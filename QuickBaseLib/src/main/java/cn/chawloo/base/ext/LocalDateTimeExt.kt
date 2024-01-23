package cn.chawloo.base.ext

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * TODO
 * @author Create by 鲁超 on 2024/1/23 09:59
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
/**
 * 转String
 * @param pattern 格式
 * @return 结果
 */
fun LocalDate.show(pattern: String): String = this.format(DateTimeFormatter.ofPattern(pattern))

/**
 * 转String
 * @param pattern 格式
 * @return 结果
 */
fun LocalDateTime.show(pattern: String): String = this.format(DateTimeFormatter.ofPattern(pattern))

/**
 * String 转 {@link LocalDate}
 */
fun DateTimeFormatter.parseLocalDate(text: String): LocalDate = LocalDate.parse(text, this)

/**
 * String 转 {@link LocalDateTime}
 */
fun DateTimeFormatter.parseLocalDateTime(text: String): LocalDateTime = LocalDateTime.parse(text, this)

/**
 * 日期转日期时间
 * @param time 传入需要设置的时间 default now
 * @return LocalDateTime
 */
fun LocalDate.toLocalDateTime(time: LocalTime = LocalTime.now()): LocalDateTime = LocalDateTime.of(this.year, this.monthValue, this.dayOfMonth, time.hour, time.minute, time.second)

/**
 * 读取当年最小月份
 */
val LocalDate.minMonth: Int
    get() = this.withMonth(1).monthValue

/**
 * 读取当月最大天数
 */
val LocalDate.maxDayOfMonth: Int
    get() = this.lengthOfMonth()

/**
 * 读取当年最小月份
 */
val LocalDate.minDayOfMonth: Int
    get() = this.withDayOfMonth(1).dayOfMonth