import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private List<String> incomeButtonsList = List.of("/Работа", "/Подработка", "/Продажа", "/Активы");
    private List<String> expenditureButtonsList = List.of("/Еда", "/Жилье");

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyBoardFirstRow = new KeyboardRow();
        KeyboardRow keyBoardSecondRow = new KeyboardRow();

        switch (sendMessage.getText()) {
            case "Выберите категорию дохода":
                keyBoardFirstRow.add(new KeyboardButton("/Работа"));
                keyBoardFirstRow.add(new KeyboardButton("/Подработка"));

                keyBoardSecondRow.add(new KeyboardButton("/Продажа"));
                keyBoardSecondRow.add(new KeyboardButton("/Активы"));
                break;
            case "Выберите категорию расхода":
                keyBoardFirstRow.add(new KeyboardButton("/Еда"));
                keyBoardFirstRow.add(new KeyboardButton("/Жилье"));

                keyBoardSecondRow.add(new KeyboardButton("/Услуги"));
                keyBoardSecondRow.add(new KeyboardButton("/Транспорт"));
                break;
            default:
                keyBoardFirstRow.add(new KeyboardButton("/Доход"));
                keyBoardFirstRow.add(new KeyboardButton("/Расход"));
                keyBoardSecondRow.add(new KeyboardButton("/Статистика"));
        }

        keyboardRowList.add(keyBoardFirstRow);
        keyboardRowList.add(keyBoardSecondRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    @Override
    public void onUpdateReceived(Update update) {
//        Model model = new Model();
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/Доход":
                    sendMsg(message, "Выберите категорию дохода");
                    break;
                case "/Расход":
                    sendMsg(message, "Выберите категорию расхода");
                    break;
                case "/Статистика":
                    sendMsg(message, "Любить Димку");
                    break;
                case "/start":
                    sendMsg(message, "Выбирайте");
                    break;
                case "/Работа":
                case "/Еда":
                    sendMsg(message, "Введите сумму");
                    break;
                default:
//                    try {
//                        sendMsg(message, Weather.getWeather(message.getText(), model));
//                    } catch (IOException e) {
//                        sendMsg(message, "Город не найден!");
//                    }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "MyOrganizerHelperBot";
    }

    @Override
    public String getBotToken() {
        return "1760315484:AAFq_W0HSeYAQCAjw8OTi8aD5CkfriVaU4Y";
    }
}
