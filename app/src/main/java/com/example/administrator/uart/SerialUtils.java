package com.example.administrator.uart;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.uartdemo.SerialPort;
import com.example.uartdemo.Tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

public class SerialUtils{

	private static final  String TAG = "SerialUtils";
	private static SerialUtils serialUtils = null;
	private Handler handler;
	/** serialport **/
	private SerialPort mSerialPort ;
	private InputStream is ;
	private OutputStream os ;
	
	private static final int port = 13;
	private static final int buad = 115200;
	private static final String powerStr = "3.3V";

	/** recv Thread **/
	private RecvThread recvThread ;
	
	private boolean isHexRecv = true  ;
	private boolean isOpen = false ;

	private SerialUtils(){}

	public static SerialUtils getInstance(){
		if(serialUtils == null){
			serialUtils = new SerialUtils();
		}
		return serialUtils;
	}

	//open serialport
	public void open(){
		//open
		try {
			mSerialPort = new SerialPort(port, buad, 0);
		}catch (Exception e) {
			Log.e(TAG, "SerialPort init fail!!");
			return;
		}
		isOpen = true;
		is = mSerialPort.getInputStream();
		os = mSerialPort.getOutputStream();
		if("3.3V".equals(powerStr)){
			mSerialPort.power3v3on();
		}else if("5V".equals(powerStr)){
			mSerialPort.power_5Von();
		}else if("scan power".equals(powerStr)){
			mSerialPort.scaner_poweron();
		}else if("psam power".equals(powerStr)){
			mSerialPort.psam_poweron();
		}else if("rfid power".equals(powerStr)){
			mSerialPort.rfid_poweron();
		}
		recvThread = new RecvThread();
		recvThread.start();
		Log.i(TAG, "SerialPort init fail!!");
	}
	//close serialport
	public void close(){
		if(recvThread != null){
			recvThread.interrupt();
		}
		if(mSerialPort != null){
			if("3.3V".equals(powerStr)){
				mSerialPort.power3v3off();
			}else if("5V".equals(powerStr)){
				mSerialPort.power_5Voff();
			}else if("scan power".equals(powerStr)){
				mSerialPort.scaner_poweroff();
			}else if("psam power".equals(powerStr)){
				mSerialPort.psam_poweroff();
			}else if("rfid power".equals(powerStr)){
				mSerialPort.rfid_poweroff();
			}
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mSerialPort.close(port);
			isOpen = false;
		}
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	//send cmd
//	private void send(){
//		byte[] cmd = null;
//		String commandStr = "The message cmd";
//		Log.i("send()", commandStr);
//		if(commandStr == null){
//			Toast.makeText(context, "cmd is null", 0).show();
//		}
//		if(isHexSend){
//			cmd = Tools.HexString2Bytes(commandStr);
//		}else{
//			cmd = commandStr.getBytes();
//		}
//		try {
//			os.write(cmd);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * recv thread receive serialport data
	 * @author Administrator
	 *
	 */
	private class RecvThread extends Thread{
		@Override
		public void run() {
			super.run();
			try {
			while(!isInterrupted()){
				int size = 0;
				byte[] buffer = new byte[1024];
				if(is == null){
					return;
				}
				size = is.read(buffer);
				if(size > 0){
					onDataReceived(buffer, size);
				}
				Thread.sleep(10);
			}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * add recv data on UI
	 * @param buffer
	 * @param size
	 */
	private void onDataReceived(final byte[] buffer, final int size){
		String recv;
		if(isHexRecv){
			recv = Tools.getMeddleByte(buffer, size);
			Log.i(TAG,"[Recv(HEX)]:" + recv + "\n");
		}else{
			recv = new String(buffer, 0 , size);
			Log.i(TAG,"[Recv]:" + recv + "\n");
		}
		Message message = new Message();
		message.what = 2;
		//�����������ص����ݴ�ŵ�Message��
		message.obj = recv;
		handler.sendMessage(message);
	}
	
	/**
	 * ��ʼ����ʷ��¼
	 * @param field
	 * @param auto
	 */
//	private void initHistory(String field,AutoCompleteTextView auto){
//		SharedPreferences sp = getSharedPreferences("commad", 0);
//        String longhistory = sp.getString("history", "nothing");
//        String[]  hisArrays = longhistory.split(",");
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, hisArrays);
//      //ֻ���������50���ļ�¼
//        if(hisArrays.length > 50){
//            String[] newArrays = new String[50];
//            System.arraycopy(hisArrays, 0, newArrays, 0, 50);
//            adapter = new ArrayAdapter<String>(this,
//                    android.R.layout.simple_dropdown_item_1line, newArrays);
//        }
//        auto.setAdapter(adapter);
//        auto.setDropDownHeight(350);
//        auto.setThreshold(1);
////        auto.setCompletionHint("�����5����¼");
//        auto.setOnFocusChangeListener(new OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                AutoCompleteTextView view = (AutoCompleteTextView) v;
//                if (hasFocus) {
//                        view.showDropDown();
//                }
//            }
//        });
//	}
	/**
	 * ������ʷ��¼
	 * @param field
	 * @param auto
	 */
//	private void saveHistory(String field,AutoCompleteTextView auto) {
//		String text = auto.getText().toString();
//		SharedPreferences sp = getSharedPreferences("commad", 0);
//		String longhistory = sp.getString(field, "nothing");
//		 if (!longhistory.contains(text + ",")) {
//	            StringBuilder sb = new StringBuilder(longhistory);
//	            sb.insert(0, text + ",");
//	            sp.edit().putString("history", sb.toString()).commit();
//	        }
//	}
}
