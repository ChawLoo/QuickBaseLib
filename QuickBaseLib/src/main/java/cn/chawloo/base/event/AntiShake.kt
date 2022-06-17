package cn.chawloo.base.event

/**
 * 防抖
 * @author Create by 鲁超 on 2020/12/21 0021 17:26
 */
class AntiShake {
    private val utils = ArrayList<OneClickUtil>()

    fun check(any: Any?): Boolean {
        val flag = any?.toString() ?: Thread.currentThread().stackTrace[2].methodName
        for (util in utils) {
            if (util.methodName == flag) {
                return util.check()
            }
        }
        val clickUtil = OneClickUtil(flag)
        utils.add(clickUtil)
        return clickUtil.check()
    }

    fun check(): Boolean {
        return check(null)
    }
}