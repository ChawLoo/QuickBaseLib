package cn.chawloo.base.net

/**
 * 接口异常
 * @author Create by 鲁超 on 2020/10/9 0009 15:02
 */
data class ApiException(override var message: String) : Exception(message)