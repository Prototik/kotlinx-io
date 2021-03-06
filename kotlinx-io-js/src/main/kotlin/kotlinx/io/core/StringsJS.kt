package kotlinx.io.core

import kotlinx.io.charsets.*
import org.khronos.webgl.*

actual fun String(bytes: ByteArray, offset: Int, length: Int, charset: Charset): String {
    @Suppress("UnsafeCastFromDynamic")
    val i8: Int8Array = bytes.asDynamic() // we know that K/JS generates Int8Array for ByteBuffer
    val buffer = if (offset == 0 && length == bytes.size) i8.buffer else i8.subarray(offset, offset + length).buffer

    val view = IoBuffer(buffer, null)
    view.resetForRead()
    val packet = ByteReadPacket(view, IoBuffer.NoPool)

    return charset.newDecoder().decode(packet, Int.MAX_VALUE)
}

internal actual fun String.getCharsInternal(dst: CharArray, dstOffset: Int) {
    val length = length
    require(dstOffset + length <= dst.size)

    var dstIndex = dstOffset
    for (srcIndex in 0 until length) {
        dst[dstIndex++] = this[srcIndex]
    }
}
