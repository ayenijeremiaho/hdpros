//package com.hdpros.hdprosbackend.utils;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.Row;
//
//public class ToWalletTransactionReportExcel {
//
//    public ToWalletTransactionReportExcel() {
//        super();
//    }
//
//    public String writeExcel(EndOfDayWalletTransactionReport endOfDayWalletTransactionReport, String table, String excelFilePath) {
//        createExcelVariables(table);
//
//        //prepare excel here
//        generateReport(endOfDayWalletTransactionReport);
//
//        return createExcelFile(excelFilePath);
//    }
//
//    private void generateReport(EndOfDayWalletTransactionReport report) {
//        CellStyle headerStyle = getHeaderCellStyle(sheet);
//
//        createHeader(headerStyle);
//        createAdditionalInfo(headerStyle, report.getInstitutionName(), report.getTransactionDate());
//        createCreditRows(headerStyle, report.getOpeningBalance(), report.getCreditedToday(), report.getTotalCredit());
//        createDebitRows(headerStyle, report.getCashOutFee(), report.getDisbursedToday(), report.getTotalDebited());
//        createClosingBalRows(headerStyle, report.getExpectedClosingBalance(), report.getActualClosingBalance(), report.getDeficit());
//    }
//
//    //for Transaction report begins
//    //1. display header
//    private void createHeader(CellStyle headerStyle) {
//        Row row = sheet.createRow(0);
//
//        Cell cellNo = row.createCell(0);
//        cellNo.setCellStyle(headerStyle);
//        cellNo.setCellValue("Medusa Summary Report ");
//    }
//
//    //2. for additional header details (institution name and date)
//    private void createAdditionalInfo(CellStyle headerStyle, String institutionName, String date) {
//        Row institutionRow = sheet.createRow(2);
//        Row dateRow = sheet.createRow(3);
//
//        //keys
//        institutionRow.createCell(0).setCellValue("Institution Name:");
//        dateRow.createCell(0).setCellValue("Transaction Date:");
//
//        //institution values
//        Cell institutionValue = institutionRow.createCell(1);
//        institutionValue.setCellStyle(headerStyle);
//        institutionValue.setCellValue(institutionName);
//
//        //date values
//        Cell dateValue = dateRow.createCell(1);
//        dateValue.setCellStyle(headerStyle);
//        dateValue.setCellValue(date);
//    }
//
//    //3 credit
//    private void createCreditRows(CellStyle headerStyle, double openingBal, double creditToday, double totalAvailable) {
//        Row creditRow = sheet.createRow(5);
//
//        Cell creditRowCell = creditRow.createCell(0);
//        creditRowCell.setCellValue("CR");
//        creditRowCell.setCellStyle(headerStyle);
//
//        creditRow.createCell(1).setCellValue("Opening Balance");
//        Cell openingBalCell = creditRow.createCell(2);
//        openingBalCell.setCellValue(openingBal);
//        openingBalCell.setCellStyle(amountStyle);
//
//        Row creditRow2 = sheet.createRow(6);
//        creditRow2.createCell(1).setCellValue("Total Credit");
//        Cell totalCreditCell = creditRow2.createCell(2);
//        totalCreditCell.setCellValue(creditToday);
//        totalCreditCell.setCellStyle(amountStyle);
//
//        Row creditRow3 = sheet.createRow(7);
//        creditRow3.createCell(1).setCellValue("Total Available");
//        Cell totalAvailableCell = creditRow3.createCell(2);
//        totalAvailableCell.setCellValue(totalAvailable);
//        totalAvailableCell.setCellStyle(amountStyle);
//
//        Cell totalAvailableCell2 = creditRow3.createCell(3);
//        totalAvailableCell2.setCellValue(totalAvailable);
//        totalAvailableCell2.setCellStyle(amountStyle);
//    }
//
//    //4 debit
//    private void createDebitRows(CellStyle headerStyle, double disbursementFee, double disbursed, double totalDebited) {
//        Row debitRow = sheet.createRow(9);
//
//        Cell debitRowCell = debitRow.createCell(0);
//        debitRowCell.setCellValue("DR");
//        debitRowCell.setCellStyle(headerStyle);
//
//        debitRow.createCell(1).setCellValue("Disbursement fee");
//        Cell cashOutFeeCell = debitRow.createCell(2);
//        cashOutFeeCell.setCellValue(disbursementFee);
//        cashOutFeeCell.setCellStyle(amountStyle);
//
//        Row debitRow2 = sheet.createRow(10);
//        debitRow2.createCell(1).setCellValue("Disbursed");
//        Cell disbursedCell = debitRow2.createCell(2);
//        disbursedCell.setCellValue(disbursed);
//        disbursedCell.setCellStyle(amountStyle);
//
//        Row debitRow3 = sheet.createRow(11);
//        debitRow3.createCell(1).setCellValue("Total Debited");
//        Cell totalDebitedCell = debitRow3.createCell(2);
//        totalDebitedCell.setCellValue(totalDebited);
//        totalDebitedCell.setCellStyle(amountStyle);
//
//        Cell totalDebitedCell2 = debitRow3.createCell(3);
//        totalDebitedCell2.setCellValue(totalDebited);
//        totalDebitedCell2.setCellStyle(amountStyle);
//    }
//
//    //5 debit
//    private void createClosingBalRows(CellStyle headerStyle, double expectedBal, double actualBal, double shortage) {
//        Row closingBalRow = sheet.createRow(13);
//
//        Cell expectedBalCellKey = closingBalRow.createCell(1);
//        expectedBalCellKey.setCellValue("Expected Closing balance");
//        expectedBalCellKey.setCellStyle(headerStyle);
//
//        Cell expectedBalCell = closingBalRow.createCell(3);
//        expectedBalCell.setCellValue(expectedBal);
//        expectedBalCell.setCellStyle(amountStyle);
//
//        Row closingBalRow2 = sheet.createRow(14);
//
//        Cell actualBalCellKey = closingBalRow2.createCell(1);
//        actualBalCellKey.setCellValue("Actual Closing balance");
//        actualBalCellKey.setCellStyle(headerStyle);
//
//        Cell actualBalCell = closingBalRow2.createCell(3);
//        actualBalCell.setCellValue(actualBal);
//        actualBalCell.setCellStyle(amountStyle);
//
//        Row closingBalRow3 = sheet.createRow(15);
//
//        Cell shortageCellKey = closingBalRow3.createCell(1);
//        shortageCellKey.setCellValue("Shortage");
//        shortageCellKey.setCellStyle(headerStyle);
//
//        Cell shortageCell = closingBalRow3.createCell(3);
//        shortageCell.setCellValue(shortage);
//        shortageCell.setCellStyle(amountStyle);
//    }
//
//    //for transaction report ends
//
//}
