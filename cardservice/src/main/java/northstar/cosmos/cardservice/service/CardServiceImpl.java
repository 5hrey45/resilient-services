package northstar.cosmos.cardservice.service;

import northstar.cosmos.cardservice.dto.CardDto;
import northstar.cosmos.cardservice.entity.Card;
import northstar.cosmos.cardservice.exception.CardAlreadyExistsException;
import northstar.cosmos.cardservice.exception.ResourceNotFoundException;
import northstar.cosmos.cardservice.mapper.CardMapper;
import northstar.cosmos.cardservice.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public CardDto getCardDetails(String mobileNumber) {
        Optional<Card> optionalCard = cardRepository.findByMobileNumber(mobileNumber);
        if (optionalCard.isEmpty()) {
            throw new ResourceNotFoundException("Card not found with mobile number: " + mobileNumber);
        }

        Card card = optionalCard.get();
        return CardMapper.mapToCardDto(card, new CardDto());
    }

    @Override
    public void createCard(CardDto cardDto) {
        Optional<Card> optionalCard = cardRepository.findByMobileNumber(cardDto.getMobileNumber());
        if (optionalCard.isPresent()) {
            throw new CardAlreadyExistsException("Card already exists with mobile number: "
                    + cardDto.getMobileNumber());
        }

        Card card = CardMapper.mapToCard(cardDto, new Card());
        cardRepository.save(card);
    }

    @Override
    public void updateCard(CardDto cardDto) {
        Optional<Card> optionalCard = cardRepository.findByMobileNumber(cardDto.getMobileNumber());
        if (optionalCard.isEmpty()) {
            throw new ResourceNotFoundException("Card does not exist with mobile number: "
                    + cardDto.getCardNumber());
        }

        Card card = CardMapper.mapToCard(cardDto, optionalCard.get());
        cardRepository.save(card);
    }

    @Override
    public void deleteCard(String mobileNumber) {
        Optional<Card> optionalCard = cardRepository.findByMobileNumber(mobileNumber);
        if (optionalCard.isEmpty()) {
            throw new ResourceNotFoundException("Card does not exist with mobile number: "
                    + mobileNumber);
        }

        Card card = optionalCard.get();
        cardRepository.delete(card);
    }
}
