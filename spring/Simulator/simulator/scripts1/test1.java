//package simulator.scripts;
//
//import jxl.*;
//import jxl.read.biff.BiffException;
//import java.io.*;
//
//public class test1 {
//
//	/**
//	 * @param args
//	 * @throws IOException
//	 */
//	public static boolean isInteger(String in) {
//		for (int i = 0; i < in.length(); i++) {
//			if (in.charAt(i) < '0' || in.charAt(i) > '9')
//				return false;
//		}
//		return true;
//	}
//
//	public static String remAlphabets(String in) {
//		char newString[] = new char[in.length() + 1];
//		int k = 0;
//		for (int i = 0; i < in.length(); i++) {
//			if (in.charAt(i) >= '0' && in.charAt(i) <= '9')
//				newString[k++] = in.charAt(i);
//		}
//		String returnString = new String(newString, 0, k);
//		// System.out.println(returnString);
//		return returnString;
//	}
//
//	public static void main(String args[]) throws Exception {
//		parse(new File("swtt with PFDN.xls"), new File("platformlistBVI.xls"),
//				"BORIVALI");
//	}
//
//	public static void parse(File inputWorkbook, File inputWorkbook2,
//			String station) throws IOException {
//		// File inputWorkbook = new File("tester.xls");
//		// File inputWorkbook2 = new File("platformlistBVI.xls");
//		Workbook w, w2;
//		try {
//			w = Workbook.getWorkbook(inputWorkbook);
//			w2 = Workbook.getWorkbook(inputWorkbook2);
//			// Get the first sheet
//			Sheet sheet;
//			Sheet sheet2 = w2.getSheet(0);
//			String direction = "";
//			String trainFile = "";
//			String entry = "";
//			String fileEntry = "";
//
//			for (int s = 0; s < 1; s++) {
//				sheet = w.getSheet(s);
//
//				if (sheet.getName().toUpperCase().contains("UP"))
//					direction = "UP";
//				else
//					direction = "DN";
//
//				int trainNumberLine = 0;
//				int stationLine = 0;
//				int directionLine = 0;
//
//				// String station="BORIVALI";
//
//				for (int i = 0; i < sheet.getRows(); i++) {
//					if (sheet.getCell(0, i).getContents().trim()
//							.equals("STATIONS")) {
//						trainNumberLine = i + 1;
//						// System.out.println(trainNumberLine);
//					}
//					if (sheet.getCell(0, i).getContents().trim()
//							.equals(station)) {
//						stationLine = i;
//						// System.out.println(stationLine);
//
//					}
//					if (sheet.getCell(0, i).getContents().trim()
//							.equals("TRAIN NO.")) {
//						directionLine = i - 1;
//						// System.out.println(directionLine);
//						break;
//					}
//				}
//
//				for (int i = 0; i < sheet.getRows(); i++) {
//					int rowC = i;
//					Cell dep;
//
//					if (sheet.getCell(0, i).getContents().trim()
//							.equals("STATIONS")) {
//						String trainNumber = "";
//						int count = 0;
//						for (int j = 2; j < sheet.getColumns() - 1; j += 2)	{
//							count = 0;
//							entry = "";
//							int flag = 0;
//							boolean through = false;
//							if (sheet.getCell(j, i + 4).getContents().trim()
//									.equals("T\\LINE")
//									|| sheet.getCell(j, i + 4).getContents()
//											.trim().equals("T/LINE"))
//								through = true;
//							for (int curRow = i + 5; curRow < i + 43; curRow++) {
//
//								if (sheet.getCell(0, curRow).getContents()
//										.trim().equals(""))
//									continue;
//								Cell trainNo = sheet.getCell(j, i + 1);
//								Cell arr = (Cell) sheet.getCell(j, curRow);
//
//								Cell plat = sheet.getCell(j + 1, curRow);
//								if (sheet.getCell(0, curRow + 1).getContents()
//										.trim().equals("")) {
//									dep = sheet.getCell(j, curRow + 1);
//									if (plat.getContents().trim().equals(""))
//										plat = sheet.getCell(j + 1, curRow + 1);
//								} else {
//									dep = sheet.getCell(j, curRow);
//								}
//
//								Cell returnT = sheet.getCell(j, i
//										+ (directionLine - stationLine));
//
//								String arrival = arr.getContents().trim();
//								arrival = arrival.replaceAll("�", "");
//								String departure = dep.getContents().trim();
//								departure = departure.replaceAll("�", "");
//								String platform = plat.getContents().trim();
//								platform = platform.replaceAll(" ", "");
//								// platform = platform.replaceAll("A", "111");
//								String returnTime = returnT.getContents()
//										.trim();
//
//								trainNumber = trainNo.getContents().trim();
//								trainNumber = trainNumber.replaceAll(" ", "");
//								// trainNumber = trainNumber.replaceAll("A",
//								// "111");
//
//								/*
//								 * if (trainNumber.contains("SPL")) //several of
//								 * them are called SPL so this gives them unique
//								 * train id { trainNumber =
//								 * trainNumber.replaceAll("SPL", "999"); String
//								 * splNo = ""; if (direction.equals("DN")) {
//								 * splNo += "1"; } splNo +=
//								 * arrival.replaceAll(":", "") +
//								 * departure.replaceAll(":", ""); trainNumber =
//								 * trainNumber + splNo; }
//								 */
//								int downPF = 1;
//								int upPF = 2;
//								int downTPF = 3;
//								int upTPF = 4;
//
//								String stationq = sheet.getCell(0, curRow)
//										.getContents().trim();
//								if (stationq.equalsIgnoreCase("Mahalakshmi")) {
//									downPF = 2;
//									upPF = 3;
//								} else if (stationq.equalsIgnoreCase("Bandra")) {
//									downPF = 2;
//									upPF = 3;
//									downTPF = 4;
//									upTPF = 5;
//								} else if (stationq
//										.equalsIgnoreCase("Vasai Road")) {
//									downPF = 2;
//									upPF = 3;
//									downTPF = 4;
//									upTPF = 5;
//								}
//								// else if(stationq.equalsIgnoreCase("Virar")) {
//								// downPF = 2; upPF=3; downTPF=4; upTPF=5;}
//								else if (stationq.equalsIgnoreCase("Andheri")) {
//									upPF = 3;
//									downTPF = 4;
//									upTPF = 5;
//								} else if (stationq
//										.equalsIgnoreCase("Bhayandar")) {
//									upPF = 4;
//									downTPF = 5;
//									upTPF = 6;
//								} else if (stationq
//										.equalsIgnoreCase("Borivali")) {
//									upPF = 3;
//									downTPF = 4;
//									upTPF = 5;
//								}
//
//								if (platform.trim().equals(""))
//									if (through)
//										platform = String.valueOf(downTPF);
//									else
//										platform = String.valueOf(downPF);
//
//								if ((!isInteger(trainNumber) || trainNumber
//										.equals(""))
//										&& !trainNumber.contains("SPL")) {
//									trainNo = sheet.getCell(j, i + 1);
//									if (isInteger(trainNo.getContents().trim()))
//										trainNumber = trainNo.getContents()
//												.trim();
//									else {
//										trainNo = sheet.getCell(j, i + 1);
//										if (isInteger(trainNo.getContents()
//												.trim()))
//											trainNumber = trainNo.getContents()
//													.trim();
//										else
//											trainNumber = "NF";
//									}
//								}
//
//								// both of these need to be checked against the
//								// second source file
//								/*
//								 * if (platform.equals("") ||
//								 * !isInteger(platform)) { int column = 0; if (s
//								 * > 0) column = 11; else { column = 0; }
//								 * 
//								 * for (int k = 0; k < sheet2.getRows(); k++) {
//								 * 
//								 * //System.out.println(sheet2.getCell(0,k).
//								 * getContents().trim()); if
//								 * (remAlphabets(sheet2
//								 * .getCell(column,k).getContents
//								 * ().trim()).equals(trainNumber)) {
//								 * //System.out
//								 * .println(sheet2.getCell(1,k).getContents
//								 * ().trim() + " " + trainNumber); platform =
//								 * sheet2
//								 * .getCell(column+1,k).getContents().trim();
//								 * //System.out.println(platform+" "+
//								 * trainNumber); break; } else platform = "NF";
//								 * }
//								 * 
//								 * }
//								 */
//								if (trainNumber == "") {
//									flag = 1;
//									break;
//								}
//								if (curRow == i + 5) {// entry=trainNumber+", ";
//									System.out.println("peeyush" + trainNumber);
//								}
//								// entry += sheet.getCell(0,
//								// curRow).getContents() + ",";
//
//								if (arrival.contains(":")) // something in
//															// arrival row
//								{
//									if (departure.contains(":")) // Case 1:
//																	// something
//																	// in
//																	// arrival
//																	// and dep
//																	// so wait
//									{
//										count++;
//										entry += sheet.getCell(0, curRow)
//												.getContents().trim()
//												+ ", PF-" + platform + ", ";
//										/*
//										 * if(arrival.endsWith("\"") ){
//										 * arrival.replaceAll("\"", "");
//										 * arrival.replaceAll(":", ""); int
//										 * arrv=Integer.parseInt(arrival);
//										 * if(arrv) }
//										 */
//										entry += arrival + ", ";
//										entry += departure + ", ";
//										trainFile += "\"" + trainNumber + "\","
//												+ direction + "\n";
//
//									} else // Case 2: arrival but no dep so
//											// terminating and return
//									{
//										count++;
//										entry += sheet.getCell(0, curRow)
//												.getContents().trim()
//												+ ", PF-" + platform + ", ";
//										entry += arrival + ", ";
//
//										/*
//										 * if (!returnTime.equals("")) { entry
//										 * += returnTime; entry += " "; }
//										 */
//
//										entry += arrival + ", ";
//
//										trainFile += "\"" + trainNumber
//												+ "\", " + direction + "\n";
//									}
//
//								} else // nothing in arrival row
//								{
//									if (departure.contains(":")) // Case 3:
//																	// quick
//																	// stop and
//																	// continue
//									{
//										count++;
//										entry += sheet.getCell(0, curRow)
//												.getContents().trim()
//												+ ", PF-" + platform + ", ";
//										entry += departure + ", "; // arrival =
//																	// departure
//										entry += departure + ", ";
//										trainFile += "\"" + trainNumber + "\","
//												+ direction + "\n";
//									} else // Case 4: neither so ignore
//									{
//
//										// entry = "A";
//									}
//								}
//
//								// entry = entry.replaceAll(":", "");
//
//							}
//							if (flag == 0) {
//								entry += "\n\n";
//								fileEntry += trainNumber + ", " + count + ", "
//										+ entry;
//							}
//						}
//
//					}
//				}
//			}
//
//			FileWriter fstream = new FileWriter("DNfinal.txt");
//			BufferedWriter out = new BufferedWriter(fstream);
//			out.flush();
//			out.write(fileEntry);
//			out.close();
//
//			FileWriter fstream2 = new FileWriter("train.txt");
//			BufferedWriter out2 = new BufferedWriter(fstream2);
//			out2.flush();
//			out2.write(trainFile);
//			out2.close();
//
//			System.out
//					.println("Successfully written to files entry.txt and train.txt");
//
//		} catch (BiffException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/*
//	 * There would be files: Station file: DOING THIS BY HAND station.txt
//	 * Station name(in quotes) Station code
//	 * 
//	 * Platform file: DOING THIS BY HAND platform.txt StationCode
//	 * PlatformNameString
//	 * 
//	 * Train file: train.txt TrainNumber Direction //is he color coding
//	 * 
//	 * Entry file: entry.txt StationCode PlatformNameString TrainNumber
//	 * ArrivalTime DepartureTime
//	 * 
//	 * quotes for platform and trainnumber to avoid crazy characters
//	 */
//
//}



