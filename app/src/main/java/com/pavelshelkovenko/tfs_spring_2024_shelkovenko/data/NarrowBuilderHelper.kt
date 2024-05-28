package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data

import org.json.JSONArray
import org.json.JSONObject

class NarrowBuilderHelper {

    fun getNarrowArrayWithObjectStructure(topicName: String, streamName: String): JSONArray {
        val jsonObjStream = JSONObject()
        jsonObjStream.put(OPERAND_KEY, streamName)
        jsonObjStream.put(OPERATOR_KEY, OPERATOR_VALUE_STREAM)

        val jsonObjTopic = JSONObject()
        jsonObjTopic.put(OPERAND_KEY, topicName)
        jsonObjTopic.put(OPERATOR_KEY, OPERATOR_VALUE_TOPIC)

        val jsonArray = JSONArray()
        jsonArray.put(jsonObjStream)
        jsonArray.put(jsonObjTopic)
        return jsonArray
    }

    fun getNarrowArrayWithArrayStructure(topicName: String, streamName: String): JSONArray {
        val jsonArrayStream = JSONArray()
        jsonArrayStream.put(OPERATOR_VALUE_STREAM)
        jsonArrayStream.put(streamName)

        val jsonArrayTopic = JSONArray()
        jsonArrayTopic.put(OPERATOR_VALUE_TOPIC)
        jsonArrayTopic.put(topicName)

        val commonJsonArray = JSONArray()
        commonJsonArray.put(jsonArrayStream)
        commonJsonArray.put(jsonArrayTopic)
        return commonJsonArray
    }

    companion object {
        private const val OPERAND_KEY = "operand"
        private const val OPERATOR_KEY = "operator"
        private const val OPERATOR_VALUE_STREAM = "stream"
        private const val OPERATOR_VALUE_TOPIC = "topic"
    }
}