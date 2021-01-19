package me.maxandroid.hilibrary.restful

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

open class HiRestful constructor(private val baseUrl: String, callFactory: HiCall.Factory){
    private var interceptors: MutableList<HiInterceptor> = mutableListOf()
    private var methodService: ConcurrentHashMap<Method, MethodParser> = ConcurrentHashMap()
    private val scheduler by lazy { Scheduler(callFactory, interceptors) }

    fun addInterceptor(interceptor: HiInterceptor) {
        interceptors.add(interceptor)
    }

    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service), object : InvocationHandler {
                //bugFix:此处需要考虑 空参数
                override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {

                    var methodParser = methodService.get(method)
                    if (methodParser == null) {
                        methodParser = MethodParser.parse(baseUrl, method)
                        methodService[method] = methodParser
                    }

                    //bugFix：此处 应当考虑到 methodParser复用，每次调用都应当解析入参
                    val request = methodParser.newRequest(method, args)
                    return scheduler.newCall(request)
                }

            }) as T
    }
}