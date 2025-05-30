package com.example.demo.utilities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateDateTime {
	static int flag = 0;

	public static String createDateTimeFolder() {
		if (flag == 0) {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH-mm-ss");
			String time = dateFormat.format(now);
			time = "report\\" + time;
			File dir = new File(time);
			dir.mkdirs();
			flag = 1;
			return time;
		} else
			return "";
	}

	public static void makeDirectory(String folderpath) {
		File dir = new File(folderpath);
		dir.mkdir();
	}

	public static String getCurrentSystemDateAndYear() {
		Calendar mCalendar = Calendar.getInstance();
		String monthNumber = new SimpleDateFormat("MM").format(mCalendar.getTime());
		String yearVal = new SimpleDateFormat("YYYY").format(mCalendar.getTime());
		int dateNum = mCalendar.get(Calendar.DATE);
		String currentDateAndYear = yearVal + monthNumber + Integer.toString(dateNum);
		return currentDateAndYear;
	}

	// Pass val argument 0 for get hour
	// 1 for get minutes
	// 2 for get seconds
	public static int getHourAndMinuteForCurrentSystemTime(int val) {
		Calendar mCalendar = Calendar.getInstance();
		String timeValue = new SimpleDateFormat("HH mm ss").format(mCalendar.getTime());
		String[] stringArr = timeValue.split(" ");
		if (val == 0)
			return Integer.parseInt(stringArr[0]);
		else if (val == 1)
			return Integer.parseInt(stringArr[1]);
		else if (val == 2)
			return Integer.parseInt(stringArr[2]);
		else {
			System.out.println("Problem in value");
			return 0;
		}

	}

	public static String startDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -7);
		Date dateBefore7Days = cal.getTime();
		return dateFormat.format(dateBefore7Days);
	}

	public static String endDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -7);
		Date dateBefore7Days = cal.getTime();
		return dateFormat.format(dateBefore7Days);
	}
	
	public static String createNewExcelFolder(){
		String newFolderLoc = ".\\src\\test\\resources\\TestData\\" + new SimpleDateFormat("dd MMM yyyy HH-mm-ss").format(new Date());
		new File(newFolderLoc).mkdir();
		return newFolderLoc;
	}

}
