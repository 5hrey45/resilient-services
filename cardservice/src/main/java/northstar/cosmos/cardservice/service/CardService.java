package northstar.cosmos.cardservice.service;

import northstar.cosmos.cardservice.dto.CardDto;

public interface CardService {

    CardDto getCardDetails(String mobileNumber);

    void createCard(CardDto cardDto);

    void updateCard(CardDto cardDto);

    void deleteCard(String mobileNumber);
}
