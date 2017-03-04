import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.model.Thread;

import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.*;

public class GmailManager {
    /**
     * List all Messages of the user's mailbox.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me"
     * can be used to indicate the authenticated user.
     * @throws IOException
     */
    public static List<Message> listMessages(Gmail service, String userId, List<String> Dates, List<String> Time, List<String> Subjects) throws IOException {
        ListMessagesResponse response = service.users().messages().list(userId).execute();

        List<Message> messages = new ArrayList<Message>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(userId).setPageToken(pageToken).execute();
            } else {
                break;
            }
        }

        List<MessagePartHeader> messageHeaders = new ArrayList<MessagePartHeader>();

        for (Message message : messages) {
            Message tempMessage = service.users().messages().get(userId, message.getId()).execute(); //retrieves the actual *entire message*
            MessagePart tempPart = tempMessage.getPayload();
            List <MessagePartHeader> tempHeader = tempPart.getHeaders();
            //Now you have the userID retrieved using a message ID from the messages in the list

            long epoch = tempMessage.getInternalDate();

            String convDate = new java.text.SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date (epoch));
            String convTime = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date (epoch));
            for(MessagePartHeader headerVar : tempHeader)
            {
                if(headerVar.getName().equalsIgnoreCase("subject"))
                {
                    //put the date and subject into the map
                    Dates.add(convDate);
                    Time.add(convTime);
                    Subjects.add(headerVar.getValue());
                }
            }
        }
        return messages;
    }

}
