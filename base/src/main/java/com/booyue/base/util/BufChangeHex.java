package com.booyue.base.util;

public class BufChangeHex {

	/**
	 * ���ڽ���ʮ������ַ�������Сд�ַ�����
	 */
	private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * ���ڽ���ʮ������ַ������Ĵ�д�ַ�����
	 */
	private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * ���ֽ�����ת��Ϊʮ������ַ�����
	 * 
	 * @param data
	 *            byte[]
	 * @return ʮ�����char[]
	 */
	public static char[] encodeHex(byte[] data) {
		return encodeHex(data, true);
	}

	/**
	 * 字节转整型
	 * @param src
	 * @param offset
	 * @return
     */
	public static int bytesToInt(byte[] src, int offset) {
		int value;
		value = ((src[offset] & 0xFF)
				| ((src[offset + 1] & 0xFF) << 8)
				| ((src[offset + 2] & 0xFF) << 16)
				| ((src[offset + 3] & 0xFF) << 24));
		return value;
	}

	/**
	 *
	 * @param data
	 *            byte[]
	 * @param toLowerCase
	 */
	public static char[] encodeHex(byte[] data, boolean toLowerCase) {
		return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	/**
	 * ���ֽ�����ת��Ϊʮ������ַ�����
	 * 
	 * @param data
	 *            byte[]
	 * @param toDigits
	 *            ���ڿ��������char[]
	 * @return ʮ�����char[]
	 */
	protected static char[] encodeHex(byte[] data, char[] toDigits) {
		int l = data.length;
		char[] out = new char[(l << 1) + l];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
			out[j++] = ' ';
		}
		return out;
	}

	protected static char[] encodeHex2(byte[] data, char[] toDigits) {
		int l = data.length;
		char[] out = new char[(l << 1)];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return out;
	}

	/**
	 * ���ֽ�����ת��Ϊʮ������ַ�
	 * 
	 * @param data
	 *            byte[]
	 * @return ʮ�����String
	 */
	public static String encodeHexStr(byte[] data) {
		return encodeHexStr(data, true);
	}

	/**
	 * ���ֽ�����ת��Ϊʮ������ַ�
	 * 
	 * @param data
	 *            byte[]
	 * @param toLowerCase
	 *            <code>true</code> ������Сд��ʽ �� <code>false</code> �����ɴ�д��ʽ
	 * @return ʮ�����String
	 */
	public static String encodeHexStr(byte[] data, boolean toLowerCase) {
		return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	/**
	 * ���ֽ�����ת��Ϊʮ������ַ�
	 * 
	 * @param data
	 *            byte[]
	 * @param toDigits
	 *            ���ڿ��������char[]
	 * @return ʮ�����String
	 */
	protected static String encodeHexStr(byte[] data, char[] toDigits) {
		return new String(encodeHex(data, toDigits));
	}

	/**
	 * ��ʮ������ַ�����ת��Ϊ�ֽ�����
	 * 
	 * @param data
	 *            ʮ�����char[]
	 * @return byte[]
	 * @throws RuntimeException
	 *             ���Դʮ������ַ�������һ����ֵĳ��ȣ����׳�����ʱ�쳣
	 */
	public static byte[] decodeHex(char[] data) {

		int len = data.length;

		if ((len & 0x01) != 0) {
			throw new RuntimeException("Odd number of characters.");
		}

		byte[] out = new byte[len >> 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f = f | toDigit(data[j], j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}

		return out;
	}

	/**
	 * ��ʮ������ַ�ת����һ������
	 * 
	 * @param ch
	 *            ʮ�����char
	 * @param index
	 *            ʮ������ַ����ַ������е�λ��
	 * @return һ������
	 * @throws RuntimeException
	 *             ��ch����һ���Ϸ���ʮ������ַ�ʱ���׳�����ʱ�쳣
	 */
	protected static int toDigit(char ch, int index) {
		int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new RuntimeException("Illegal hexadecimal character "
					+ ch + " at index " + index);
		}
		return digit;
	}

	public static void main(String[] args) {
		String srcStr = "��ת���ַ�";
		String encodeStr = encodeHexStr(srcStr.getBytes());
		String decodeStr = new String(decodeHex(encodeStr.toCharArray()));
		System.out.println("ת��ǰ��" + srcStr);
		System.out.println("ת����" + encodeStr);
		System.out.println("��ԭ��" + decodeStr);
	}

	/**
	 * Byte转Bit
	 */
	public static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) +
				(byte) ((b >> 6) & 0x1) +
				(byte) ((b >> 5) & 0x1) +
				(byte) ((b >> 4) & 0x1) +
				(byte) ((b >> 3) & 0x1) +
				(byte) ((b >> 2) & 0x1) +
				(byte) ((b >> 1) & 0x1) +
				(byte) ((b >> 0) & 0x1);
	}

	
	

}
