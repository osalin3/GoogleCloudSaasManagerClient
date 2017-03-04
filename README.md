# GoogleCloudSaasManagerClient
An excerpt of client written to manage Gmail and Google Sheets SaaS Cloud Applications using Google's RESTful APIs

The client simply listens for new messages to a gmail account and extracts DDMMYYYY,
as well as subject, and inserts this information into a corresponding cell in a Sheet.
The application retrieves a list of emailIDs from the inbox, which is used to retrieve 
necessary header information. The header values are used for the extraction, parsing, 
and conversion of the target information that is inserted into the Sheet.

The OAuth 2.0 protocol is used for authentication and authorization of the client, allowing
it to interact with the Gmail and Sheets RESTful APIs used to manage the SaaS cloud applications.
