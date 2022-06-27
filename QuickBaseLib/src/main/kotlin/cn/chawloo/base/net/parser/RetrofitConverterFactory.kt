package cn.chawloo.base.net.parser

import cn.chawloo.base.ext.json
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * java-serialization-json解析Retrofit工厂
 * @author Create by 鲁超 on 2022/5/13 0013 16:12:12
 *----------Dragon be here!----------/
 *       ┌─┐      ┌─┐
 *     ┌─┘─┴──────┘─┴─┐
 *     │              │
 *     │      ─       │
 *     │  ┬─┘   └─┬   │
 *     │              │
 *     │      ┴       │
 *     │              │
 *     └───┐      ┌───┘
 *         │      │神兽保佑
 *         │      │代码无BUG！
 *         │      └──────┐
 *         │             ├┐
 *         │             ┌┘
 *         └┐ ┐ ┌───┬─┐ ┌┘
 *          │ ┤ ┤   │ ┤ ┤
 *          └─┴─┘   └─┴─┘
 *─────────────神兽出没───────────────/
 */
class RetrofitConverterFactory private constructor(private val contentType: MediaType, private val serializer: Serializer) : Converter.Factory() {
    companion object {
        private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaType()
        fun create(contentType: MediaType = MEDIA_TYPE, jsonSerialization: Json = json): RetrofitConverterFactory =
            RetrofitConverterFactory(contentType, Serializer.FromString(jsonSerialization))
    }

    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit): Converter<ResponseBody, *> =
        DeserializationStrategyConverter(serializer.serializer(type), serializer)

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> = SerializationStrategyConverter(contentType, serializer.serializer(type), serializer)
}