package com.example.telegrambot.handler;


import java.io.IOException;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import com.example.telegrambot.App;
import com.example.telegrambot.bot.Bot;
import com.example.telegrambot.command.Command;
import com.example.telegrambot.command.ParsedCommand;

//import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class IpHandler extends AbstractHandler{
//    private static final Logger log = Logger.getLogger(IpHandler.class);
    private final String END_LINE = "\n";

	public IpHandler(Bot bot) {
		super(bot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
		Command command = parsedCommand.getCommand();
//        String text = parsedCommand.getText();
//        StringBuilder result = new StringBuilder();
//        Set<String> emojisInTextUnique = new HashSet<>(EmojiParser.extractEmojis(text));
//        if (emojisInTextUnique.size() > 0) result.append("Parsed emojies from message:").append("\n");
//        for (String emojiUnicode : emojisInTextUnique) {
//            Emoji byUnicode = EmojiManager.getByUnicode(emojiUnicode);
//            log.debug(byUnicode.toString());
//            String emoji = byUnicode.getUnicode() + " " +
//                    byUnicode.getAliases() +
//                    " " + byUnicode.getDescription();
//            result.append(emoji).append("\n");
//        }
    //    return result.toString();

        switch (command) {
            case IP:
                bot.sendQueue.add(getMessageIp(chatId, update));
                break;
            case ID:
                return "Your telegramID: " + update.getMessage().getFrom().getId();
		default:
			break;
        }
        return "";
	}
	
    private SendMessage getMessageIp(String chatID, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);

    	String url = App.prop.getProperty("Router_ip_page");
    //	getIp(url);

        StringBuilder text = new StringBuilder();
        text.append("*This is IP message*").append(END_LINE).append(END_LINE);
        text.append("IP Address: " + getIp(url, update)).append(END_LINE);
      
        sendMessage.setText(text.toString());
        return sendMessage;
    }
    
    private String getIp(String page, Update update) {
    	String[] url = {page};
    	String ip = "";
//    	int[] iip = new int[3];
    	try {
    		Document doc = Jsoup.connect(url[0]).get();
    		String user = "" + update.getMessage().getFrom().getId();
    		String ovn = App.prop.getProperty("User_id");
    		if(user.equals(ovn)) {
    				ip = doc.select("html > body > div:eq(1) > center > table > tbody > tr > td:eq(2)").first().text();	
    		}
    	else {
    		ip = "Кто ты такой? Я тебя не знаю";
    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return ip;
    }
    

}
