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
    val sc = Sparksession.sparkContext
    sc.setLogLevel("ERROR")


    // Question 1
    println("QUESTION 1")
    val df = Sparksession.read.parquet("prepared_trainingset")

    // Question 2
    println("QUESTION 2")

    //a
    val tokenizer = new RegexTokenizer()
      .setPattern("\\W+")
      .setGaps(true)
      .setInputCol("text")
      .setOutputCol("tokens")
    //b
    val remover = new StopWordsRemover()
      .setInputCol("tokens")
      .setOutputCol("filtered")
    //c
    val cv = new CountVectorizer()
      .setInputCol("filtered")
      .setOutputCol("rawFeatures")
    //d
    val idf = new IDF().setInputCol("rawFeatures").setOutputCol("tfidf")

    val pipeline = new Pipeline()
      .setStages(Array(tokenizer, remover,cv,idf))

    val model =pipeline.fit(df)
    val df2 = model.transform(df)
    println(df2.show())

    // Question 3
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
    //h
    val assembler = new VectorAssembler()
      .setInputCols(Array("tfidf", "days_campaign", "hours_prepa", "goal", "country_vec", "currency_vec"))
      .setOutputCol("features")

    //i
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

    //j
    val pipelineAll = new Pipeline()
      .setStages(Array(tokenizer, remover,cv,idf,countryIndexer, currencyIndexer,encoder,assembler,lr))

    val modelAll =pipelineAll.fit(df)
    val dfALL = modelAll.transform(df)
    println(dfALL.show())

    // Question 5`
    println("QUESTION 5")

    // k
    val Array(training, test) = df.randomSplit(Array(0.9, 0.1), seed = 12345)

    //l
    val f1 = new MulticlassClassificationEvaluator()
      .setMetricName("f1")
      .setLabelCol("final_status")
      .setPredictionCol("predictions")


    val paramGrid = new ParamGridBuilder()
      .addGrid(lr.regParam, Array(10e-8, 10e-6, 10e-4,10e-2))
      .addGrid(cv.minDF,Array(55.0,75.0,95.0))
      .build()

    val trainValidationSplit = new TrainValidationSplit()
      .setEstimator(pipelineAll)
      .setEvaluator(f1)
      .setEstimatorParamMaps(paramGrid)
      .setTrainRatio(0.7)

    val modelLR = trainValidationSplit.fit(training)

    //m
    val df_WithPredictions =modelLR.transform(test)
    //n
    df_WithPredictions.groupBy("final_status", "predictions").count.show()


  }
}
