package com.example.core_database.converter

import androidx.room.TypeConverter
import com.example.core_model.models.ClassSchedule
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromScheduleList(value: List<ClassSchedule>?): String {
        return gson.toJson(value ?: emptyList<ClassSchedule>())
    }

    @TypeConverter
    fun toScheduleList(value: String?): List<ClassSchedule> {
        if (value.isNullOrBlank()) return emptyList()
        val type = object : TypeToken<List<ClassSchedule>>() {}.type
        return gson.fromJson(value, type)
    }
}