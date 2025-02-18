package hu.webuni.booking.web;

import hu.webuni.bonus.api.BonusApi;
import hu.webuni.booking.dto.PurchaseData;
import hu.webuni.booking.dto.TicketData;
import hu.webuni.currency.api.CurrencyApi;
import hu.webuni.flights.api.FlightsApi;
import hu.webuni.flights.dto.Airline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BookingController {

    private static final String USD = "USD";

    @Value("${booking.bonus}")
    double bonusRate;

    @Autowired
    BonusApi bonusApi;
    @Autowired
    CurrencyApi currencyApi;
    @Autowired
    FlightsApi flightsApi;

    @PostMapping("/ticket")
    public PurchaseData buyTicket(@RequestBody TicketData ticketData) {
        PurchaseData purchaseData = new PurchaseData();

        List<Airline> airlines = flightsApi.searchFlight(ticketData.getFrom(), ticketData.getTo());
        if (CollectionUtils.isEmpty(airlines)) {
            purchaseData.setSuccess(false);
            return purchaseData;
        }

        Map<Double, Airline> map = new HashMap<>();
        airlines.forEach(al -> {
            String currency = al.getCurrency();
            if (USD.equals(currency)) {
                map.put(al.getPrice(), al);
            } else {
                double rate = currencyApi.getRate(currency, USD);
                map.put(al.getPrice() * rate, al);
            }
        });

        Airline airline = map.get(Collections.min(map.keySet()));
        double price = airline.getPrice();

        String user = ticketData.getUser();
        if (ticketData.isUseBonus()) {
            double points = bonusApi.getPoints(user);

            double newPrice = 0;

            if (points > price) {
                newPrice = points - price;
            } else if (price > points) {
                newPrice = price - points;
            } else if (price == points) {
                newPrice = price - points;
            }

            double bonusUsed = price - newPrice;
            bonusApi.addPoints(user, -bonusUsed);
            purchaseData.setBonusUsed(bonusUsed);
            price = newPrice;
        }

        double bonusEarned = bonusRate * price;
        bonusApi.addPoints(user, bonusEarned);

        purchaseData.setBonusEarned(bonusEarned);
        purchaseData.setPrice(price);
        purchaseData.setSuccess(true);
        return purchaseData;
    }
}
