import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class Client {
    public static void main(String[] args) throws Exception {

        Scanner console = new Scanner(System.in);
        long currentTimeMillis = System.currentTimeMillis();
        System.out.print("Enter you username : ");
        String name = console.nextLine();
        String id =  name + "_" + String.valueOf(currentTimeMillis)  ;
        
        ////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////

        // SET THESE FROM JSONBIN.IO BEFORE EXECUTION
        // String myToken = "ACCESS_TOKEN";
        // String myMasterKey = "MASTER_TOKEN";
        // String collectionID = "COLLECTION_ID";
   
        final AtomicReference<String> serverAddress = new AtomicReference<>("localhost");
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
        try {
            String jsonData = Request.fetchBins(collectionID, myMasterKey , null, "ascending");
            jsonData = jsonData.substring(1, jsonData.length() - 1);
            String[] items = jsonData.split("\\},\\{");
            String binID = "";

            for (String item : items) {
                String[] parts = item.split("\"record\":\"");
                binID = parts[1].split("\"")[0]; 
            }
            String response = Request.get(binID , myMasterKey ,myToken);

            int ipStartIndex = response.indexOf("\"ip\":\"") + "\"ip\":\"".length();
            int ipEndIndex = response.indexOf("\"", ipStartIndex);
            String ipAddress = response.substring(ipStartIndex, ipEndIndex);

            serverAddress.set(ipAddress);
            System.out.println("Success 🥳🥳🎉");

        } catch (Exception e) {
            System.out.print("Enter IP addres manually : "  );
            serverAddress.set(console.nextLine());
        }
    });


    try {
        future.get(); 
    } catch (InterruptedException | ExecutionException e) {
        System.out.println("An error occurred: " + e.getMessage());
    }

    System.out.println("Connecting to server with ip "+ serverAddress.get());
    Socket socket = new Socket(serverAddress.get(), 12345);

    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        new Thread(new Runnable() {
            public void run() {
                try {
                    Scanner in = new Scanner(socket.getInputStream());
                    while (in.hasNextLine()) {
                        String message = in.nextLine();
                        HashMap<String,String> rmap = MapHash.parse(message);

                        if (rmap.containsKey("type") && rmap.get("type").equals("game")) {
                            Executors.newSingleThreadExecutor().execute(new Runnable() {
                                public void run() {
                                    if (rmap.get("name").equals("typeracer")) {
                                        System.out.println("Running typeracer");
                                        Games.typeracer(out , id);
                                    }
                                    System.out.println("Game ended");
                                }
                            });
                            System.out.println(message + "Done with if");
                        }else if (rmap.containsKey("type") && rmap.get("type").equals("game_data")) {
                            System.out.println("Runnign elif with data" + rmap);
                            if(!rmap.get("type").equals(id)) {
                                System.out.println(rmap.get("data") + " from " + rmap.get("id"));
                            }else {
                                System.out.println(rmap.get("data") + "Shouldnt get this" );
                            }
                            System.err.println("end of line");
                        } else {
                            System.out.println(rmap.get("payload"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your messages (exit to quit):");

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) break;
            HashMap<String, String> ms = new HashMap<>();
            ms.put("id", id);
            ms.put("payload", input);
            String ds = MapHash.unparse(ms);
            out.println(ds); 

        }
        socket.close();
    }

}