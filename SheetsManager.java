import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SheetsManager
{

    public static void main(String[] args) throws IOException {
        List<String> Dates = new ArrayList<String>();
        List<String> Time = new ArrayList<String>();
        List<String> Subjects = new ArrayList<String>();
        Gmail service1 = GoogleAuthenticator.getGmailService();
        GmailManager serv1 = new GmailManager();
        serv1.listMessages(service1, "osalin3uic@gmail.com", Dates, Subjects, Time);

        // Build a new authorized API client service.
        Sheets service = GoogleAuthenticator1.getSheetsService();

        List<List<Object>> writeData = new ArrayList<>();
        List<Object> dataRow = new ArrayList<>();
        dataRow.add("Date");
        dataRow.add("Time");
        dataRow.add("Subject");
        writeData.add(dataRow);
        ValueRange vr = new ValueRange().setValues(writeData).setMajorDimension("ROWS");
        SpreadsheetProperties properties = new SpreadsheetProperties();
        properties.setTitle(Dates.get(0));
        Spreadsheet newSheet = new Spreadsheet();
        newSheet.setProperties(properties);
        Sheets.Spreadsheets.Create spreadsheet = service.spreadsheets().create(newSheet);
        String sheetID = spreadsheet.execute().getSpreadsheetId();

        for(int i  = 0; i < (Dates.size()); i++)
        {
            //System.out.println("Date: " + Dates.get(i) + " Time: " + Subjects.get(i) + " Subject: " + Time.get(i));
            List<Object> tempData = new ArrayList<>();
            tempData.add(Dates.get(i));
            tempData.add(Subjects.get(i));
            tempData.add(Time.get(i));
            writeData.add(tempData);
        }
        service.spreadsheets().values().update(sheetID, "Sheet1!A1:C", vr).setValueInputOption("RAW").execute();
    }


}