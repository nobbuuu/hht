package com.example.lamemp3;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Process;

import com.booyue.base.util.LoggerUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MP3Recorder {
    private static final String TAG = "MP3Recorder";
	private String mDir = null;
	private String mFilePath = null;
//	private int sampleRate = 0;
	private boolean isRecording = false;
	private boolean isPause = false;
	private Handler handler = null;
	private int mVolume = 1;

	/**
	 * 开始录音
	 */
	public static final int MSG_REC_STARTED = 1;

	/**
	 * 结束录音
	 */
	public static final int MSG_REC_STOPPED = 2;

	/**
	 * 暂停录音
	 */
	public static final int MSG_REC_PAUSE = 3;

	/**
	 * 继续录音
	 */
	public static final int MSG_REC_RESTORE = 4;
	/**
	 * 音量大小
	 */
	public static final int MSG_VOLUME_CHANGE = 5;
	/**
	 * 缓冲区挂了,采样率手机不支持
	 */
	public static final int MSG_ERROR_GET_MIN_BUFFERSIZE = -1;

	/**
	 * 创建文件时扑街了
	 */
	public static final int MSG_ERROR_CREATE_FILE = -2;

	/**
	 * 初始化录音器时扑街了
	 */
	public static final int MSG_ERROR_REC_START = -3;

	/**
	 * 录音的时候出错
	 */
	public static final int MSG_ERROR_AUDIO_RECORD = -4;

	/**
	 * 编码时挂了
	 */
	public static final int MSG_ERROR_AUDIO_ENCODE = -5;

	/**
	 * 写文件时挂了
	 */
	public static final int MSG_ERROR_WRITE_FILE = -6;

	/**
	 * 没法关闭文件流
	 */
	public static final int MSG_ERROR_CLOSE_FILE = -7;

	public static final int SAMPLE_RATE = 8000;
	public static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
	public static final int  AUDIO_FORMAT= AudioFormat.ENCODING_PCM_16BIT;

//	初始化录制参数 quality:0=很好很慢 9=很差很快
	public static final int  QUALITY= 2;

	public MP3Recorder(String dir) {
//		this.sampleRate = 44100;
		this.mDir = dir;
	}

	/**
	 * 开片
	 */
	public void start(final String fileName) {
		if (isRecording) {
			return;
		}

		new Thread() {
			@Override
			public void run() {
				//1.检测存放录音文件的目录是否存在
				String fileDir = StorageUtil.getSDPath() + mDir;
				File dir = new File(fileDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				//2.创建录音文件
				mFilePath = StorageUtil.getSDPath() + mDir + fileName + ".mp3";
				LoggerUtils.d(TAG + "record file path : " + mFilePath);
				//设置线程等级
				Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
				//4、 根据定义好的几个配置，来获取合适的缓冲大小
				final int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG,AUDIO_FORMAT);
				if (minBufferSize < 0) {
					if (handler != null) {
						handler.sendEmptyMessage(MSG_ERROR_GET_MIN_BUFFERSIZE);
					}
					return;
				}
				//5、获取录音对象
				AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
						CHANNEL_CONFIG,AUDIO_FORMAT, minBufferSize * 2);

				// 6、创建录音的缓冲空间和转mp3的缓冲空间   5秒的缓冲
				short[] buffer = new short[SAMPLE_RATE * (16 / 8) * 1 * 5];
				byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];
				//7、创建输出流
				FileOutputStream output = null;
				try {
					output = new FileOutputStream(new File(mFilePath));
				} catch (FileNotFoundException e) {
					sendMessage(MSG_ERROR_CREATE_FILE);
					return;
				}
				//8、本地MP3转换器的初始化
				MP3Recorder.init(SAMPLE_RATE, 1, SAMPLE_RATE, 32);
				isRecording = true; // 录音状态
				isPause = false; // 录音状态
				try {
					try {
						//9、开始录音动作
						audioRecord.startRecording(); // 开启录音获取音频数据
					} catch (IllegalStateException e) {
						// 不给录音...
						sendMessage(MSG_ERROR_REC_START);
						return;
					}

					try {
						// 开始录音
						sendMessage(MSG_REC_STARTED);

						int readSize = 0;
						boolean pause = false;
						//10、如果正在录音中，循环读取数据并转换成MP3
						long time = System.currentTimeMillis();
						while (isRecording) {
							// 11、暂停进行原地循环 暂停
							if (isPause) {
								if (!pause) {
									sendMessage(MSG_REC_PAUSE);
									pause = true;
								}
								continue;
							}
							//12、恢复录音
							if (pause) {
								sendMessage(MSG_REC_RESTORE);
								pause = false;
							}

							// 实时录音写数据
							readSize = audioRecord.read(buffer, 0,minBufferSize);
//							LoggerUtils.d(TAG + "---readSize: " + readSize);

							// 计算分贝值
							long v = 0;
		                    // 将 buffer 内容取出，进行平方和运算
		                    for (int i = 0; i < buffer.length; i++) {
		                        v += buffer[i] * buffer[i];
		                    }
		                    // 平方和除以数据总长度，得到音量大小。
		                    double mean = v / (double) readSize;
		                    double volume = 10 * Math.log10(mean);
		                    volume =  volume * readSize / 32768 - 1;
		                    LoggerUtils.d(TAG, "volume: " + volume);
		                    if(volume > 0){
		                    	mVolume = (int) (Math.abs(volume) * 100);
		                    }else{
		                    	mVolume = 1;
		                    }
							LoggerUtils.d(TAG, "mVolume: " + volume);
		                    if(System.currentTimeMillis() - time > 300){
								sendMessage(MSG_VOLUME_CHANGE);
								time = System.currentTimeMillis();
							}
							if (readSize < 0) {
								sendMessage(MSG_ERROR_AUDIO_RECORD);
								break;
							} else if (readSize == 0) {
								;
							} else {
								int encResult = MP3Recorder.encode(buffer,buffer, readSize, mp3buffer);
								if (encResult < 0) {
									sendMessage(MSG_ERROR_AUDIO_ENCODE);
									break;
								}
								if (encResult != 0) {
									try {
										output.write(mp3buffer, 0, encResult);
									} catch (IOException e) {
										sendMessage(MSG_ERROR_WRITE_FILE);
										break;
									}
								}
							}
						}
						// 录音完
						int flushResult = MP3Recorder.flush(mp3buffer);
						if (flushResult < 0) {
							sendMessage(MSG_ERROR_AUDIO_ENCODE);
						}
//						if (flushResult != 0) {
						if (flushResult >= 0) {
							try {
								output.write(mp3buffer, 0, flushResult);
								LoggerUtils.d(TAG + "mp3buffer.size = " + mp3buffer.length + "");
							} catch (IOException e) {
								sendMessage(MSG_ERROR_WRITE_FILE);
							}
						}
						try {
							output.close();
						} catch (IOException e) {
							sendMessage(MSG_ERROR_CLOSE_FILE);
						}
					} finally {
						audioRecord.stop();
						audioRecord.release();
					}
				} finally {
					MP3Recorder.close();
					isRecording = false;
				}
				sendMessage(MSG_REC_STOPPED);
			}
		}.start();
	}

	public void stop() {
		isRecording = false;
	}

	public void pause() {
		isPause = true;
	}

	public void restore() {
		isPause = false;
	}

	public boolean isRecording() {
		return isRecording;
	}

	public boolean isPause() {
		if (!isRecording) {
			return true;
		}
		return isPause;
	}

	public String getFilePath() {
		return mFilePath;
	}

	/**
	 * 获取分贝
	 * @return
	 */
	public int getVolume(){
		return mVolume;
	}

	/**
	 * 发送消息
	 * @param what 消息的类型
	 */
	public void sendMessage(int what){
		if(handler != null){
			handler.sendEmptyMessage(what);
		}
	}

	/**
	 * 录音状态管理
	 *
	 * RecMicToMp3#MSG_REC_STARTED
	 * RecMicToMp3#MSG_REC_STOPPED
	 * RecMicToMp3#MSG_REC_PAUSE
	 * RecMicToMp3#MSG_REC_RESTORE
	 * RecMicToMp3#MSG_ERROR_GET_MIN_BUFFERSIZE
	 * RecMicToMp3#MSG_ERROR_CREATE_FILE
	 * RecMicToMp3#MSG_ERROR_REC_START
	 * RecMicToMp3#MSG_ERROR_AUDIO_RECORD
	 * RecMicToMp3#MSG_ERROR_AUDIO_ENCODE
	 * RecMicToMp3#MSG_ERROR_WRITE_FILE
	 * RecMicToMp3#MSG_ERROR_CLOSE_FIL
	 */
	public void setHandle(Handler handler) {
		this.handler = handler;
	}

	// 以下为Native部分
	static {
		System.loadLibrary("mp3lame");
	}

	/**
	 * 初始化录制参数
	 * MP3Recorder.init(SAMPLE_RATE, 2, SAMPLE_RATE, 32);
	 */
	public static void init(int inSamplerate, int outChannel, int outSamplerate, int outBitrate) {
		init(inSamplerate, outChannel, outSamplerate, outBitrate, QUALITY);
	}

	/**
	 * 初始化录制参数 quality:0=很好很慢 9=很差很快
	 */
	public native static void init(int inSamplerate, int outChannel,
			int outSamplerate, int outBitrate, int quality);

	/**
	 * 音频数据编码(PCM左进,PCM右进,MP3输出)
	 * // 5秒的缓冲
	 * 	short[] buffer = new short[SAMPLE_RATE * (16 / 8) * 1 * 5];
	 *	byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];
	 *  readSize = audioRecord.read(buffer, 0,minBufferSize);
	 *  MP3Recorder.encode(buffer,buffer, readSize, mp3buffer);
	 */
	public native static int encode(short[] buffer_l, short[] buffer_r, int samples, byte[] mp3buf);

	/**
	 * 刷干净缓冲区
	 */
	public native static int flush(byte[] mp3buf);

	/**
	 * 结束编码
	 */
	public native static void close();
}
