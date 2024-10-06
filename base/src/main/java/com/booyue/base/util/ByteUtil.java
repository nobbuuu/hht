package com.booyue.base.util;

/**
 * @author: wangxinhua
 * @date: 2018/11/28
 * @description :
 */
public class ByteUtil {
    private static final String TAG = "ByteUtil";
    /**
     * 字节拼接
     *
     * @param cmd
     * @param p1
     * @param p2
     * @param p3
     * @return
     */
//    public static byte[] byteConnect(byte[] cmd, int p1, int p2, int p3) {
//        LoggerUtils.d(TAG + "___指令：" + Arrays.toString(cmd) + ",参数1：" + p1 + ",参数2：" + p2 + ",参数3：" + p3);
//        byte[] send = new byte[15];
//        int headLength = head.length;
//        int cmdLength = cmd.length;
//        byte[] p1Bytes = intToByteArray4(p1);
//        byte[] p2Bytes = intToByteArray4(p2);
//        byte[] p3Bytes = intToByteArray4(p3);
//        System.arraycopy(head, 0, send, 0, headLength);
//        System.arraycopy(cmd, 0, send, headLength, cmdLength);
//        System.arraycopy(p1Bytes, 0, send, headLength + cmdLength, p1Bytes.length);
//        System.arraycopy(p2Bytes, 0, send, headLength + cmdLength + p1Bytes.length, p2Bytes.length);
//        System.arraycopy(p3Bytes, 0, send, headLength + cmdLength + p1Bytes.length + p2Bytes.length, p3Bytes.length);
//        return concat(newHead, send);
//    }

    /**
     * 连接字节数组
     *
     * @param a
     * @param b
     * @return
     */
    private byte[] concat(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    /**
     * int转byteArray
     *
     * @param i
     * @return
     */
    public static byte[] intToByteArray4(int i) {
        byte[] result = new byte[4];
        result[3] = (byte) ((i >> 24) & 0xFF);
        result[2] = (byte) ((i >> 16) & 0xFF);
        result[1] = (byte) ((i >> 8) & 0xFF);
        result[0] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * 整型转字节数组
     * @param i
     * @return
     */
    public static byte[] intToByteArray1(int i) {
        byte[] result = new byte[2];
        byte[] r = new byte[1];
        result[1] = (byte) ((i >> 8) & 0xFF);
        result[0] = (byte) (i & 0xFF);
        r[0] = result[0];
        return r;
    }
}
