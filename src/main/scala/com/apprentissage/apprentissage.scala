package com.apprentissage



import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.evaluation.{BinaryClassificationEvaluator, MulticlassClassificationEvaluator, RegressionEvaluator}
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.ml.feature._
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.ml.tuning.{ParamGridBuilder, TrainValidationSplit}
import org.apache.spark.{SparkConf, SparkContext, sql}




object apprentissage {



  def main(args: Array[String]): Unit = {
    //create spark conf and context
    val spconf = new SparkConf()
    spconf.setMaster("local")
    spconf.setAppName("apprentissage")
    var Sparksession = new sql.SparkSession.Builder()
      .appName("apprentissage")
      .config(spconf)
      .getOrCreate()
    // Gestion des affichages
    val sc = Sparksession.sparkContext
    sc.setLogLevel("ERROR")


    // Question 1 - Lecture des données
    println("QUESTION 1")
    val df = Sparksession.read.parquet("prepared_trainingset")

    // Question 2
    println("QUESTION 2")

    //a - Separation du texte en mots ( tokenisation )
    val tokenizer = new RegexTokenizer()
      .setPattern("\\W+")
      .setGaps(true)
      .setInputCol("text")
      .setOutputCol("tokens")
    //b - Supression des mots sans valeur présent dans le text
    val remover = new StopWordsRemover()
      .setInputCol("tokens")
      .setOutputCol("filtered")
    //c - Representation du décompte des tokens sous forme de vecteur
    val cv = new CountVectorizer()
      .setInputCol("filtered")
      .setOutputCol("rawFeatures")
    //d - TF-IDF
    val idf = new IDF().setInputCol("rawFeatures").setOutputCol("tfidf")

    val pipeline = new Pipeline()
      .setStages(Array(tokenizer, remover,cv,idf))

    val model =pipeline.fit(df)
    val df2 = model.transform(df)
    println(df2.show())

    // Question 3 - Conversion des données catégorielles en données numérique pour modelisation
    println("QUESTION 3")

    //e
    val countryIndexer = new StringIndexer()
      .setInputCol("country2")
      .setOutputCol("country_indexed")
      .setHandleInvalid("skip")

    //f
    val currencyIndexer = new StringIndexer()
      .setInputCol("currency2")
      .setOutputCol("currency_indexed")
      .setHandleInvalid("skip")
    //g

    val encoder = new OneHotEncoderEstimator()
      .setInputCols(Array("country_indexed", "currency_indexed"))
      .setOutputCols(Array("country_vec", "currency_vec"))

    val pipeline2 = new Pipeline()
      .setStages(Array(countryIndexer, currencyIndexer,encoder))

    val model2 =pipeline2.fit(df2)
    val df3 = model2.transform(df2)
    println(df3.show())

    // Question 4

    println("QUESTION 4")
    //h - Selection des features pour calculer notre modèle
    val assembler = new VectorAssembler()
      .setInputCols(Array("tfidf", "days_campaign", "hours_prepa", "goal", "country_vec", "currency_vec"))
      .setOutputCol("features")

    //i - Parametrage du modèle
    val lr = new LogisticRegression()
      .setElasticNetParam(0.0)
      .setFitIntercept(true)
      .setFeaturesCol("features")
      .setLabelCol("final_status")
      .setStandardization(true)
      .setPredictionCol("predictions")
      .setRawPredictionCol("raw_predictions")
      .setThresholds(Array(0.7, 0.3))
      .setTol(1.0e-6)
      .setMaxIter(300)

    //j - Creation du pipeline combinant les précedentes étapes
    val pipelineAll = new Pipeline()
      .setStages(Array(tokenizer, remover,cv,idf,countryIndexer, currencyIndexer,encoder,assembler,lr))

    val modelAll =pipelineAll.fit(df)
    val dfALL = modelAll.transform(df)
    println(dfALL.show())

    // Question 5
    println("QUESTION 5")

    // k
    val Array(training, test) = df.randomSplit(Array(0.9, 0.1), seed = 12345)

    //l
    // Choix de la metric pour selectionné les hyper parametre
    val f1 = new MulticlassClassificationEvaluator()
      .setMetricName("f1")
      .setLabelCol("final_status")
      .setPredictionCol("predictions")

    // Choix des hyper-parametre à tester
    val paramGrid = new ParamGridBuilder()
      .addGrid(lr.regParam, Array(10e-8, 10e-6, 10e-4,10e-2))
      .addGrid(cv.minDF,Array(55.0,75.0,95.0))
      .build()

    // Mise en place du modèle
    val trainValidationSplit = new TrainValidationSplit()
      .setEstimator(pipelineAll)
      .setEvaluator(f1)
      .setEstimatorParamMaps(paramGrid)
      .setTrainRatio(0.7)

    val modelLR = trainValidationSplit.fit(training)

    //m
    val df_WithPredictions =modelLR.transform(test)

    // Evaluation du modèle sur le jeu de test
    val F1 = f1.evaluate(df_WithPredictions)
    println(s"F1 score = $F1")
    //n
    df_WithPredictions.groupBy("final_status", "predictions").count.show()


  }
}
