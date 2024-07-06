package northstar.cosmos.cardservice.mapper;

import northstar.cosmos.cardservice.dto.CardDto;
import northstar.cosmos.cardservice.entity.Card;

public class CardMapper {

    public static CardDto mapToCardDto(Card Card, CardDto CardDto) {
        CardDto.setMobileNumber(Card.getMobileNumber());
        CardDto.setCardNumber(Card.getCardNumber());
        CardDto.setCardType(Card.getCardType());
        CardDto.setTotalLimit(Card.getTotalLimit());
        CardDto.setAmountUsed(Card.getAmountUsed());
        CardDto.setAvailableAmount(Card.getAvailableAmount());
        return CardDto;
    }

    public static Card mapToCard(CardDto CardDto, Card Card) {
        Card.setMobileNumber(CardDto.getMobileNumber());
        Card.setCardNumber(CardDto.getCardNumber());
        Card.setCardType(CardDto.getCardType());
        Card.setTotalLimit(CardDto.getTotalLimit());
        Card.setAmountUsed(CardDto.getAmountUsed());
        Card.setAvailableAmount(CardDto.getAvailableAmount());
        return Card;
    }
}
