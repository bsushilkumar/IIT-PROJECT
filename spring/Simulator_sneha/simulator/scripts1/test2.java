package simulator.scripts1;


import globalVariables.GlobalVar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import simulator.util.Time;

//import jxl.Cell;
//import jxl.Sheet;
//import jxl.Workbook;
//import jxl.read.biff.BiffException;

public class test2 extends test5 {
	/**
	 * @param args
	 */
	public static void main(String args[]) {
		// addDownTrains("down.txt", false, "trainDiagram.txt");
	}

	public static HashSet<Train> addDownTrains(String filename, boolean append,
			String trainDiagramFileName) {
		HashSet<Train> downTrainList = new HashSet<Train>();
		try {
			ArrayList<Platform> platformList = new ArrayList<Platform>();
			StreamTokenizer st = new StreamTokenizer(new FileReader("loop.txt"));
			String line;
			while (st.nextToken() != StreamTokenizer.TT_EOF) {
				int loopNo = (int) st.nval;
				st.nextToken();
				st.nextToken();
				st.nextToken();
				String stationName = st.sval;
				st.nextToken();
				String platformName = st.sval;

				int skip = 3 + 7 + 7 + 7;
				for (int i = 0; i < skip; i++) {
					st.nextToken();
				}

				// System.out.println(loopNo + " " + stationName + " "
				// + platformName);
				Platform platform = new Platform(stationName, platformName,
						loopNo);
				platformList.add(platform);
			}

			// System.exit(0);
			Workbook wb = WorkbookFactory.create(new File(
					"proposedTimeTable.xlsx"));
			Sheet sheet = wb.getSheetAt(0);
			Cell cell = sheet.getRow(0).getCell(0);
			// cell.setCellType(Cell.CELL_TYPE_STRING);
			// cell.setCellValue("a test");
			int maxRowNo = 2550; // max is 2566
			int stationColNo = 0;
			int minColNo = 2;
			int maxColNo = 17;
			int relativeCarRow = 3;
			int relativeTrainIdRow = 2;
			int relativeTrainNoRow = 1;
			int relativeTurnAroundDepartureNo = 43;
			int relativeRakeLinkNo = 44;
			int relativeStartReadNo = 5;
			int relativeEndReadNo = 42;
			int startPlatformRelativeRowNow = 4;

			String downThroughPlatform = "PF-3";
			String downLocalPlatform = "PF-1";
			String previousPlatformString = "";
			String platformName = "";

			System.out.println("Read");
			System.out.println(sheet.getRow(0).getCell(0).getDateCellValue());
			System.out.println("TimeString check - "
					+ getTimeString(sheet, 0, 2));
			System.out.println("TimeString check - "
					+ sheet.getRow(0).getCell(2).getDateCellValue());
			System.out
					.println("TimeString check - " + getStringAt(sheet, 0, 2));
			System.out
					.println("TimeString check - " + getNumberAt(sheet, 0, 2));

			// System.exit(0);
			int trainCount = 0;
			for (int sheetNo = 0; sheetNo < 1; sheetNo++) {
				sheet = wb.getSheet("myDown");

				// BufferedWriter bw = new BufferedWriter(new
				// FileWriter(filename,
				// append));
				// BufferedWriter trainDiagramBW = new BufferedWriter(
				// new FileWriter(trainDiagramFileName, append));
				BufferedWriter errorbw = new BufferedWriter(new FileWriter(
						"error.txt", false));

				for (int rowNo = 0; rowNo <= maxRowNo; rowNo++) {
					// cell = sheet.getRow(29).getCell(4);
					// String timeString = getTimeString(sheet, 15, 21);
					// System.out.println("YYYYYYYYYYYYYYYYYYYY " + timeString);
					// timeString = getStringAt(sheet, 15, 21);
					// System.out.println("YYYYYYYYYYYYYYYYYYYY " + timeString);
					//
					// System.out.println("YYYYYYYYYYYYYYYYYYYY "
					// + getNumberAt(sheet, 15, 21));
					// String matunga = getStringAt(sheet, 0, 21);
					// cell = sheet.getRow(21).getCell(15);
					// Date date = cell.getDateCellValue();
					// System.out.println("YYYYYYYYYYYYYYYYYYYY " + matunga
					// + " cell format " + cell.getCellType() + " "
					// + date.getHours() + " " + date.getMinutes() + " "
					// + date.getSeconds());
					//
					// System.exit(0);
					cell = sheet.getRow(rowNo).getCell(stationColNo);

					String findStationString = getStringAt(sheet, stationColNo,
							rowNo);
					if (!findStationString.equalsIgnoreCase("stations"))
						continue;
					// System.out.println("stations - " + findStationString
					// + " row " + rowNo + " col " + stationColNo);

					for (int colNo = minColNo; colNo <= maxColNo; colNo++) {
						String carString = getStringAt(sheet, colNo,
								rowNo + relativeCarRow).toLowerCase();
						// System.out.println("carString " + carString + " row "
						// + rowNo + " col " + colNo);

						if (!carString.contains("car"))
							continue;

						trainCount++;

						int trainNo = getNumberAt(sheet, colNo, rowNo
								+ relativeTrainNoRow);
						// System.out.println("trainNo " + trainNo);

						double length = getCarLength(carString);
						// System.out.println("Length " + length);
						String trainId = getStringAt(sheet, colNo, rowNo
								+ relativeTrainIdRow);
						trainId = getActualTrainId(trainId);

						// System.out.println("trainId " + trainId);
						String directionString = "up";
						int direction = GlobalVar.UP_DIRECTION;

						previousPlatformString = getStringAt(sheet, colNo,
								rowNo + startPlatformRelativeRowNow);
						previousPlatformString = getProperPlatformString(
								previousPlatformString, direction);
						// System.out.println("previousPlatformString "
						// + previousPlatformString);

						int rakeLinkNo = getNumberAt(sheet, colNo, rowNo
								+ relativeRakeLinkNo);
						if (rakeLinkNo == -1) {
							String rakeLinkString = getStringAt(sheet, colNo,
									rowNo + relativeRakeLinkNo);
							try {
								rakeLinkNo = Integer.parseInt(rakeLinkString);
							} catch (Exception ex) {
								rakeLinkNo = -1;
							}
						}

						String returnTimeString = getTimeString(sheet, colNo,
								rowNo + relativeTurnAroundDepartureNo);

						int entryCount = 0;
						boolean goregaonDiversion = false;
						ArrayList<Entry> entryList = new ArrayList<Entry>();
						boolean harbour = false;
						boolean empty = false;
						boolean firstEntry = true;
						for (int currentRowNo = rowNo + relativeStartReadNo; currentRowNo <= rowNo
								+ relativeEndReadNo; currentRowNo++) {

							// arrivalTimeString =
							// getDisplayTimeString(arrivalTimeString);
							String stationName = getStringAt(sheet,
									stationColNo, currentRowNo);

							String checkForHarbourTrains = getStringAt(sheet,
									colNo, currentRowNo);
							if (checkForHarbourTrains.equalsIgnoreCase("CSTM")) {
								harbour = true;
							}

							String checkForEmpty = getStringAt(sheet, colNo,
									currentRowNo);
							if (checkForEmpty.equalsIgnoreCase("EMPTY")) {
								empty = true;
							}

							String checkForThroughLineString = getStringAt(
									sheet, colNo, currentRowNo);
							if (checkForThroughLineString.equalsIgnoreCase("T")
									|| checkForThroughLineString
											.equalsIgnoreCase("T/LINE")) {
								previousPlatformString = checkForThroughLineString;
							}

							String checkForGoregaonDiversion = getStringAt(
									sheet, colNo, currentRowNo);
							if (checkForGoregaonDiversion
									.equalsIgnoreCase("GMN")) {
								goregaonDiversion = true;
							}

							if (stationName.isEmpty()) {
								continue;
							}

							String tempStationName = stationName.toLowerCase();
							String arrivalTimeString = "";
							String departureTimeString = "";
							int relativePlatformRowNo = 1;

							if (tempStationName.contains("churchgate")) {
								departureTimeString = getTimeString(sheet,
										colNo, currentRowNo);
								relativePlatformRowNo = -1;
							} else if (tempStationName.contains("central")
									|| tempStationName.contains("bandra")
									|| tempStationName.contains("vasai")
									|| tempStationName.contains("virar")) {
								arrivalTimeString = getTimeString(sheet, colNo,
										currentRowNo);
								relativePlatformRowNo = 1;
							} else if (tempStationName.contains("andheri")
									|| tempStationName.contains("borivali")
									|| tempStationName.contains("dadar")) {
								arrivalTimeString = getTimeString(sheet, colNo,
										currentRowNo);
								departureTimeString = getTimeString(sheet,
										colNo, currentRowNo + 1);
								relativePlatformRowNo = 2;
							} else {
								cell = sheet.getRow(currentRowNo)
										.getCell(colNo);
								if (cell == null)
									continue;
								if (cell.getCellType() == cell.CELL_TYPE_NUMERIC
										&& !DateUtil.isCellDateFormatted(cell)) {
									continue;
								}

								arrivalTimeString = getTimeString(sheet, colNo,
										currentRowNo);
								relativePlatformRowNo = 0;
							}

							if (arrivalTimeString.isEmpty()
									&& departureTimeString.isEmpty())
								continue;

							entryCount++;

							String platformId = readPlatformName(direction,
									stationName, sheet, currentRowNo,
									relativePlatformRowNo, colNo);
							platformId = platformId.trim();
							// System.out.println("stationName " + stationName
							// + " platformId " + platformId);

							if (trainNo == 90039) {
								System.out.println("train " + trainNo
										+ " previousPlatformString "
										+ previousPlatformString
										+ " platformId " + platformId
										+ " stationName " + stationName);
							}

							if (isThroughPlatform(platformId)
									|| isLocalPlatform(platformId)) {
								previousPlatformString = platformId;
								previousPlatformString = getProperPlatformString(
										previousPlatformString, direction);
								// System.out
								// .println("previousPlatformString changed to "
								// + previousPlatformString);
								platformName = getPlatformNameForSpecialCases(
										stationName, previousPlatformString,
										direction, trainNo);
							} else if (platformId.equals("")) {
								if (stationName.equalsIgnoreCase("malad")) {
									String borivaliPlatform = getStringAt(
											sheet, colNo, currentRowNo + 4);
									borivaliPlatform = borivaliPlatform.trim();
									if (borivaliPlatform.equalsIgnoreCase("1")
											|| borivaliPlatform
													.equalsIgnoreCase("7")
											|| borivaliPlatform
													.equalsIgnoreCase("8")
											|| borivaliPlatform
													.equalsIgnoreCase("2"))
										previousPlatformString = "downLocal";
								}

								if (stationName.equalsIgnoreCase("jogeshwari")) {
									String borivaliPlatform = getStringAt(
											sheet, colNo, currentRowNo + 6);
									if (borivaliPlatform.equalsIgnoreCase("2")) {
										previousPlatformString = "downLocal";
									}

									String andheriPlatform = getStringAt(sheet,
											colNo, currentRowNo - 1);
									String goregaonString = getTimeString(
											sheet, colNo, currentRowNo + 1);
									if (!goregaonString.trim().isEmpty()
											&& !andheriPlatform.trim()
													.equalsIgnoreCase("T")) {
										previousPlatformString = "downLocal";
									}
								}

								platformName = getPlatformNameForSpecialCases(
										stationName, previousPlatformString,
										direction, trainNo);

								if (trainNo == 90039) {
									System.out.println("stationName "
											+ stationName + " platform "
											+ platformName);
								}

								if (stationName.equalsIgnoreCase("andheri")) {
									String borivaliPlatform = getStringAt(
											sheet, colNo, currentRowNo + 9);
									borivaliPlatform = borivaliPlatform.trim();
									if (borivaliPlatform.equalsIgnoreCase("1")
											|| borivaliPlatform
													.equalsIgnoreCase("7")
											|| borivaliPlatform
													.equalsIgnoreCase("8")
											|| borivaliPlatform
													.equalsIgnoreCase("2"))
										previousPlatformString = "downLocal";
								}
							} else {
								if (stationName.equalsIgnoreCase("borivali")
										&& (platformId.equalsIgnoreCase("1")
												|| platformId
														.equalsIgnoreCase("2")
												|| platformId
														.equalsIgnoreCase("3") || platformId
													.equalsIgnoreCase("4"))) {

									String dahisarTimeString = getTimeString(
											sheet, colNo, currentRowNo + 3);
									if (!dahisarTimeString.trim().isEmpty()) {
										previousPlatformString = "downLocal";
									}
								}

								platformName = "PF-" + platformId;
								if (platformId.equalsIgnoreCase("6")
										&& stationName
												.equalsIgnoreCase("borivali")) {
									// System.out.println("stationName "
									// + stationName);
									// System.exit(0);
								}
							}

							if (trainNo == 90039)
								System.out.println("trainNo " + trainNo + " "
										+ stationName + " " + arrivalTimeString
										+ " " + platformName);

							int loopNo = getLoopNoFromStationDirectionPlatform(
									stationName, direction, platformName,
									previousPlatformString, platformList);
							if (loopNo == -1) {
								// System.out.println("Error: trainNo " +
								// trainNo
								// + " has undefined platform");
								errorbw.write("Error: trainNo " + trainNo
										+ " has undefined platform at "
										+ stationName + " platformName "
										+ platformName + "\n\n");
							} else {
								entryList.add(new Entry(loopNo,
										arrivalTimeString, departureTimeString,
										haltTime, firstEntry));
								firstEntry = false;
							}

							if (trainNo == 90039) {
								System.out.println("trainNo " + trainNo + " "
										+ stationName + " " + arrivalTimeString
										+ " " + platformName + " LOOP "
										+ loopNo);
							}

						}

						// if (trainNo == 90479)
						// System.exit(0);
						String trainString = "";
						if (!harbour && trainNo != -1 && !empty) {
							// if (trainNo == 92024 || trainNo == 90729
							// || trainNo == 91207 || trainNo == 90249
							// || trainNo == 90249 || trainNo == 90965
							// || trainNo == 90881) {
							// break;
							// }

							Entry.processAllEntries(entryList);

							String trainDiagramString = trainNo + " "
									+ "SCHEDULED 0 0\n\n";
							// trainDiagramBW.write(trainDiagramString);
							Train train = new Train(trainNo, trainId,
									rakeLinkNo, directionString, length,
									acceleration, deceleration, priority,
									maxSpeed, operatingDays, entryList,
									returnTimeString);

							downTrainList.add(train);

							// trainString += trainNo + " " + trainId + " "
							// + rakeLinkNo + " " + directionString + " "
							// + length + " " + acceleration + " "
							// + deceleration + " " + priority + " "
							// + maxSpeed + " " + operatingDays + " "
							// + entryList.size() + "\t";
							// bw.write(trainString);
							// for (int i = 0; i < entryList.size(); i++) {
							// Entry entry = entryList.get(i);
							// bw.write(entry.loopNo + " "
							// + entry.arrivalTimeString + " "
							// + entry.departureTimeString + "\t");
							// }
							//
							// bw.write("\"" + returnTimeString + "\"");
							//
							// bw.write("\n\n");

							Entry firstEntry1 = entryList.get(0);
							Entry lastEntry = entryList
									.get(entryList.size() - 1);
							String startTimeString = firstEntry1.arrivalTimeString;
							String endTimeString = lastEntry.departureTimeString;
							int startTime = Time
									.getIntegerFromTimeString(startTimeString);
							int endTime = Time
									.getIntegerFromTimeString(endTimeString);
							if (endTime > startTime + 2 * 60 * 60) {
								System.out.println("Train " + trainNo
										+ " has bad entries");
								errorbw.write("Train " + trainNo
										+ " has bad entries");
							}

							// special case for bandra terminating trains
							if (firstEntry1.loopNo == 11
									|| firstEntry1.loopNo == 37) {
								firstEntry1.loopNo = 501;
							} else if (lastEntry.loopNo == 11
									|| lastEntry.loopNo == 37) {
								lastEntry.loopNo = 501;
							}
							
							// special case for dadar terminating trains
							if (firstEntry1.loopNo == 61
									|| firstEntry1.loopNo == 86) {
								firstEntry1.loopNo = 526;
							} else if (lastEntry.loopNo == 61
									|| lastEntry.loopNo == 86) {
								lastEntry.loopNo = 526;
							}

							// special train coming from bandra yard
							if (trainNo == 90077) {
								firstEntry1.loopNo = 11;
							}
						}

					}

				}

				// trainDiagramBW.close();
				// bw.close();
				errorbw.close();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Read");
		return downTrainList;
	}

	private static String getTimeString(Sheet sheet, int colNo, int rowNo) {
		Cell cell = sheet.getRow(rowNo).getCell(colNo);
		if (cell == null)
			return "";
		// System.out.println("fart - " + cell.getRichStringCellValue() + " "
		// + cell.toString() + " " + cell.getStringCellValue() + " "
		// + cell.getErrorCellValue());

		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			int hour = cell.getDateCellValue().getHours();
			int minute = cell.getDateCellValue().getMinutes();
			int second = cell.getDateCellValue().getSeconds();

			String timeString = "";
			if (hour == 0)
				timeString += "00";
			else if (hour < 10)
				timeString += "0" + hour;
			else
				timeString += hour;
			if (minute == 0)
				timeString += "00";
			else if (minute < 10)
				timeString += "0" + minute;
			else
				timeString += minute;
			if (second == 0)
				timeString += "00";
			else if (second < 10)
				timeString += "0" + second;
			else
				timeString += second;

			return timeString;
		}

		return "";
	}

	private static int getNumberAt(Sheet sheet, int colNo, int rowNo) {
		Cell cell = sheet.getRow(rowNo).getCell(colNo);
		if (cell != null) {
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC
					&& !DateUtil.isCellDateFormatted(cell)) {
				Number number = cell.getNumericCellValue();
				return number.intValue();
			}
		}

		return -1;
	}

	private static String getStringAt(Sheet sheet, int colNo, int rowNo) {
		Cell cell = sheet.getRow(rowNo).getCell(colNo);
		if (cell != null) {
			if (cell.getCellType() == Cell.CELL_TYPE_STRING)
				return cell.getStringCellValue().trim();
		}

		return "";
	}

	private static String readPlatformName(int direction, String stationName,
			Sheet sheet, int rowNo, int relativeRowNo, int colNo) {
		String tempStation = stationName;
		tempStation = tempStation.toLowerCase();
		String value;
		value = String
				.valueOf(getNumberAt(sheet, colNo, rowNo + relativeRowNo));
		// System.out.println("Value is " + value + " at colNo " + colNo
		// + " row " + (rowNo + relativeRowNo) + " station "
		// + stationName);
		if (value.equals("-1") || value.equals("0")) {
			value = getStringAt(sheet, colNo, rowNo + relativeRowNo);
			if (value.contains(" ")) {
				value = value.substring(0, value.indexOf(" "));
			} else if (value.contains("/")) {
				value = value.substring(0, value.indexOf("/"));
				if (tempStation.contains("vasai")) {
					if (value.contains("T") || value.contains("4"))
						value = "4";
				} else if (tempStation.contains("dadar")) {
					value = "1";
				}
			}
		}

		return value;

	}

	private static int getLoopNoFromStationDirectionPlatform(
			String stationName, int direction, String platformName,
			String previousPlatformString, ArrayList<Platform> platformList) {
		if (platformName == null) {
			// System.out.println("platform name is null");
			return -1;

		}

		for (Platform platform : platformList) {
			if (platform.stationName.equalsIgnoreCase(stationName)
					&& platform.platformName.contains(platformName)) {
				return platform.loopNo;
			}
		}

		return -1;
	}

	private static String getPlatformNameForSpecialCases(String stationName,
			String previousPlatformString, int direction, int trainNo) {

		stationName = stationName.toLowerCase();

		if (direction == GlobalVar.UP_DIRECTION) {
			String downThrough = "downThrough";
			String downLocal = "downLocal";
			String lineString = downThrough;

			if (previousPlatformString.isEmpty()
					|| previousPlatformString.equalsIgnoreCase(downLocal)
					|| previousPlatformString.toLowerCase().contains("ladies")
					|| previousPlatformString.contains("L"))
				lineString = downLocal;

			if (lineString.contains(downThrough)) {
				if (stationName.contains("bandra"))
					return "PF-4";
				else if (stationName.contains("andheri"))
					return "PF-4";
				else if (stationName.contains("borivali"))
					return "PF-4";
				else if (stationName.contains("bhayandar"))
					return "PF-5";
				else if (stationName.contains("vasai road"))
					return "PF-4";
				else if (stationName.contains("virar"))
					return "PF-3";
				else
					return "PF-3";
			}

			if (lineString.contains(downLocal)) {
				if (stationName.contains("vasai road"))
					return "PF-2";
				else if (stationName.contains("lakshmi")) {
					return "PF-2";
				} else if (stationName.contains("churchgate")) {
					if ((trainNo - 1) % 4 == 0) {
						return "PF-1";
					} else
						return "PF-2";
				} else
					return "PF-1";
			}
		}

		return null;
	}

	private static String getActualTrainId(String trainId) {
		int firstSpaceOccurence = trainId.indexOf(" ");
		if (firstSpaceOccurence == -1)
			return trainId;
		return trainId.substring(0, firstSpaceOccurence);
	}

	private static double getCarLength(String carString) {
		if (carString.contains("9"))
			return 0.258 * 3 / 4;
		if (carString.contains("15"))
			return 0.258 * 5 / 4;
		else
			return 0.258;
	}
}
