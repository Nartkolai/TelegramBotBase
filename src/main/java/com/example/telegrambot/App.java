package com.example.telegrambot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import com.example.telegrambot.bot.Bot;
import com.example.telegrambot.service.MessageReciever;
import com.example.telegrambot.service.MessageSender;

public class App {
//    private static final Logger log = Logger.getLogger(App.class);
	private static final int PRIORITY_FOR_SENDER = 1;
	private static final int PRIORITY_FOR_RECEIVER = 3;
	private static final String BOT_ADMIN = "321644283";
	public static Properties prop;

	public static void main(String[] args) {
		String botName = "", botToken = "";
		/* Properties */ prop = new Properties();
		try {
			InputStream input = new FileInputStream("config.properties");
			prop.load(input);
			botName = prop.getProperty("Bot_name");
			botToken = prop.getProperty("Bot_token");

		} catch (IOException ex) {
//			try {
////		        System.out.println(input);
//		        System.out.println("Input Bot name.");
//				@SuppressWarnings("resource")
//				Scanner scanner = new Scanner(System.in);
//		        String input = scanner.next();
//				prop.setProperty("Bot_name", input);
//				
//		        System.out.println("Input Bot token.");
//				scanner = new Scanner(System.in);
//		        input = scanner.next();
//				prop.setProperty("Bot_token", input);
//				
//		        System.out.println("Input router ip adress page");
//				scanner = new Scanner(System.in);
//		        input = scanner.next();
//				prop.setProperty("Router_ip_page", input);
//				
//		        System.out.println("Input you user ID.");
//				scanner = new Scanner(System.in);
//		        input = scanner.next();
//				prop.setProperty("User_id", input);
//				OutputStream output = new FileOutputStream("config.properties");
//				prop.store(output, null);
//				System.out.println("Created config file");
//			} catch (FileNotFoundException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			ex.printStackTrace();
		}
		ApiContextInitializer.init();
		Bot test_habr_bot = new Bot(botName, botToken);

		MessageReciever messageReciever = new MessageReciever(test_habr_bot);
		MessageSender messageSender = new MessageSender(test_habr_bot);

		test_habr_bot.botConnect();

		Thread receiver = new Thread(messageReciever);
		receiver.setDaemon(true);
		receiver.setName("MsgReciever");
		receiver.setPriority(PRIORITY_FOR_RECEIVER);
		receiver.start();

		Thread sender = new Thread(messageSender);
		sender.setDaemon(true);
		sender.setName("MsgSender");
		sender.setPriority(PRIORITY_FOR_SENDER);
		sender.start();

		sendStartReport(test_habr_bot);
	}

	private static void sendStartReport(Bot bot) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(BOT_ADMIN);
		sendMessage.setText("Запустился");
		bot.sendQueue.add(sendMessage);
	}
}
