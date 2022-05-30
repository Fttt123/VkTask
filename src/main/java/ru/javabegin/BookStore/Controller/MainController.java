package ru.javabegin.BookStore.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.BookStore.JsonParser;
import ru.javabegin.BookStore.entity.*;

import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class MainController {
    private static final Logger LOGGER = Logger.getLogger("MainController");
    private final MainServer mainServer;
    private final Market market;
    private final JsonParser jsonParser = new JsonParser();

    private MainController(){
        mainServer = jsonParser.FromJson();
        market = new Market(mainServer.setBooksWithoutAmount0());
        LOGGER.setLevel(Level.ALL);
        ConsoleHandler ch = new ConsoleHandler();
        LOGGER.addHandler(ch);
    }

    @GetMapping("/")
    public ResponseEntity<String> main(){
        return ResponseEntity.ok("Книжный интернет-магазин");
    }

    @GetMapping("/market")
    public ResponseEntity<Market> books(){
        market.getProducts().removeIf(product -> product.getAmount() <= 0);
        return ResponseEntity.ok(market);
    }

    @GetMapping("/account")
    public ResponseEntity<account> account(){
        return ResponseEntity.ok(mainServer.getAccount());
    }

    @PostMapping(path = "/market/deal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<account> MessageMarketDeal(@RequestBody Deal newDeal){
        if(mainServer.GetBookById(newDeal.getId()) == null){
            LOGGER.log(Level.WARNING,"No such book in the store");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(mainServer.getAccount().getBooks() == null && mainServer.getAccount().getMoney() >= mainServer.GetBookById(newDeal.getId()).getPrice() * newDeal.getAmount() && mainServer.GetBookById(newDeal.getId()).getAmount() >= newDeal.getAmount()){
            mainServer.getAccount().setBooks(new ArrayList<books>() {{add(mainServer.GetBookById(newDeal.getId())); get(0).setAmount(newDeal.getAmount());}});
            money(newDeal);
            jsonParser.ToJson(mainServer);
            return new ResponseEntity<>(mainServer.getAccount(), HttpStatus.OK);
        }
        if(mainServer.getAccount().getMoney() >= mainServer.GetBookById(newDeal.getId()).getPrice() * newDeal.getAmount() && mainServer.GetBookById(newDeal.getId()).getAmount() >= newDeal.getAmount()){
            SetBookInAccount(newDeal);
            money(newDeal);
            jsonParser.ToJson(mainServer);
            return new ResponseEntity<>(mainServer.getAccount(), HttpStatus.OK);
        }
        mistake(newDeal);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public void money(Deal newDeal){
        mainServer.getAccount().setMoney(mainServer.getAccount().getMoney() - mainServer.GetBookById(newDeal.getId()).getPrice() * newDeal.getAmount());
        mainServer.GetBookById(newDeal.getId()).setAmount(mainServer.GetBookById(newDeal.getId()).getAmount() - newDeal.getAmount());
        LOGGER.log(Level.INFO,"user bought the book " + mainServer.GetBookById(newDeal.getId()).getName() + " in quantity " + newDeal.getAmount());
    }
    public void SetBookInAccount(Deal newDeal) {
        if(mainServer.getAccount().checkId(newDeal.getId())){
            books newBook = MakeNewBook(newDeal);
            mainServer.getAccount().getBooks().add(newBook);
            return;
        }
        mainServer.getAccount().GetBookById(newDeal.getId()).setAmount(mainServer.getAccount().GetBookById(newDeal.getId()).getAmount() + newDeal.getAmount());
    }

    public books MakeNewBook(Deal newDeal) {
        books NewBook = new books(mainServer.GetBookById(newDeal.getId()).getId(),mainServer.GetBookById(newDeal.getId()).getName());
        NewBook.setAuthor(mainServer.GetBookById(newDeal.getId()).getAuthor());
        NewBook.setAmount(newDeal.getAmount());
        NewBook.setPrice(mainServer.GetBookById(newDeal.getId()).getPrice());
        return NewBook;
    }

    @PostMapping(path = "/account/setMoney", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<account> MessageSetMoney(@RequestBody Deal newAccount){
        mainServer.getAccount().setMoney(mainServer.getAccount().getMoney() + Math.abs(newAccount.getMoney()));
        jsonParser.ToJson(mainServer);
        LOGGER.log(Level.INFO,"Money transferred to the account in the amount of " + newAccount.getMoney());
        return new ResponseEntity<>(mainServer.getAccount(), HttpStatus.OK);
    }

    public void mistake(Deal newDeal){
        if(mainServer.getAccount().getMoney() < mainServer.GetBookById(newDeal.getId()).getPrice() * newDeal.getAmount()){
            LOGGER.log(Level.WARNING,"lack of money");
            return;
        }
        LOGGER.log(Level.WARNING,"Not enough books in the store");
    }
}
