package jatools.dataset;

import java.io.PrintStream;


public class DatasetPrinter {
    private Dataset dataset;

    public DatasetPrinter(Dataset dataset) {
        this.dataset = dataset;
    }

    public void printBuffers11(PrintStream printStream) {
        printBuffers(printStream, true);
    }

    public void printBuffers(PrintStream printStream, boolean data) {
        StringBuffer sep = new StringBuffer(18 * dataset.getColumnCount());
        sep.append("*");

        for (int i = 0; i < dataset.getColumnCount(); i++)
            sep.append("******************");

        String sep1 = sep.toString();
        
        sep = new StringBuffer(18 * dataset.getColumnCount());
        sep.append("+");

        for (int i = 0; i < dataset.getColumnCount(); i++)
            sep.append("-----------------+");

        String sep2 = sep.toString();

        if (data) {
            printStream.println(sep1);
            printStream.println("Databuffer row count: "+dataset.getRowCount());
            printColumnNames(printStream);
            printStream.println(sep2);
            printRows(printStream);
            printStream.println(sep2);
        }

        printStream.println(sep1);
    }

    private void printRows(PrintStream printStream) {
        int countRows;
        int countColumns;
        Row row;
        countRows = dataset.getRowCount();
        countColumns = dataset.getColumnCount();

        for (int i = 0; i < countRows; i++) {
            StringBuffer stringBuffer = createSpaceFilledStringBuffer(countColumns * 18);
            row = dataset.getRow(i);
            RowsService.setStringToStringBuffer(stringBuffer, "|", 0);

            for (int j = 0; j < countColumns; j++) {
                String string = "" + row.getValueAt(j);

                if (string.length() > 17) {
                    string = string.substring(0, 17);
                }

                RowsService.setStringToStringBuffer(stringBuffer, string, (j * 18) + 1);
                RowsService.setStringToStringBuffer(stringBuffer, "|", j * 18);
            }

            RowsService.setStringToStringBuffer(stringBuffer, " ", (countColumns * 18) - 1);
            printStream.print(stringBuffer.toString());
            printStream.println();
        }
    }

    private void printColumnNames(PrintStream printStream) {
        int countColumns;

        countColumns = dataset.getColumnCount();

        StringBuffer stringBuffer = createSpaceFilledStringBuffer(countColumns * 18);

        for (int i = 0; i < countColumns; i++) {
            RowsService.setStringToStringBuffer(stringBuffer,
                " " + dataset.getRowInfo().getColumnName(i) + " ", i * 18);
        }

        printStream.print(stringBuffer.toString());
        printStream.println();
    }

    private StringBuffer createSpaceFilledStringBuffer(int size) {
        StringBuffer stringBuffer = new StringBuffer(size);

        for (int i = 0; i < size; i++) {
            stringBuffer.append(" ");
        }

        return stringBuffer;
    }
}
