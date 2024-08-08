package org.example.englishByHeart.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.englishByHeart.controller.CustomResponse;
import org.example.englishByHeart.dto.*;
import org.example.englishByHeart.domain.*;
import org.example.englishByHeart.enums.SortBy;
import org.example.englishByHeart.repos.RuleRepository;
import org.example.englishByHeart.repos.SentenceRepository;
import org.example.englishByHeart.repos.TopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SentenceService {

    @Autowired
    private SentenceRepository sentenceRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private TopicService topicService;

    private static final Logger logger = LoggerFactory.getLogger(SentenceService.class);

    public List<Sentence> getAllSentences() {
        return sentenceRepository.findAll();
    }

    public Optional<Sentence> getSentenceById(Long id) {
        return sentenceRepository.findById(id);
    }

    public CustomResponse<SentenceIdResponse> createSentence(SentenceDTO sentenceDTO) {

        CustomResponse<SentenceIdResponse> response; // Specify type argument here

        try {
            Sentence sentence = new Sentence();
            sentence.setLearningSentence(sentenceDTO.getLearningSentence());
            sentence.setComment(sentenceDTO.getComment());
            sentence.setUserLink(sentenceDTO.getUserLink());
            sentence.setUserId(sentenceDTO.getUserId());

            List<SentenceTopic> sentenceTopics = new ArrayList<>();

            for (Long topicsId : sentenceDTO.getTopicsIds()) {
                Topic topic = topicRepository.findById(topicsId)
                        .orElseThrow(() -> new RuntimeException("Topic not found with id: " + topicsId));
                SentenceTopic sentenceTopic = new SentenceTopic();
                sentenceTopic.setSentence(sentence);
                sentenceTopic.setTopic(topic);
                sentenceTopics.add(sentenceTopic);
            }
            sentence.setSentenceTopics(sentenceTopics);

            List<SentenceRule> sentenceRules= new ArrayList<>();

            for (Long rulesId : sentenceDTO.getRulesIds()) {
                Rule rule = ruleRepository.findById(rulesId)
                        .orElseThrow(() -> new RuntimeException("Rule not found with id: " + rulesId));
                SentenceRule sentenceRule= new SentenceRule();
                sentenceRule.setSentence(sentence);
                sentenceRule.setRule(rule);
                sentenceRules.add(sentenceRule);
            }
            sentence.setSentenceRules(sentenceRules);

            Sentence createdSentence = sentenceRepository.save(sentence);
            Long sentenceId = createdSentence.getSentenceId();

            // Populate the response
            response = new CustomResponse<>(true, "", new SentenceIdResponse(sentenceId));

        } catch (Exception e) {
            // If there's an error, populate the error message and set success to false
            response = new CustomResponse<>(false, "Failed to create sentence: " + e.getMessage(), null);
        }

        return response;
    }

    public ResponseEntity<Sentence> updateSentence(Long id, Sentence updatedSentence) {
        Optional<Sentence> sentenceOptional = sentenceRepository.findById(id);
        if (sentenceOptional.isPresent()) {
            updatedSentence.setSentenceId(id);
            Sentence savedSentence = sentenceRepository.save(updatedSentence);
            return new ResponseEntity<>(savedSentence, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public List<Sentence> getSentencesByRule(Long ruleId) {
        return sentenceRepository.findByRuleId(ruleId);
    }

    public Sentence getSentenceBySentenceId(Long sentenceId) {
        Optional<Sentence> optionalSentence = sentenceRepository.findBySentenceId(sentenceId);
        return optionalSentence.orElseThrow(() -> new NoSuchElementException("Sentence not found with id: " + sentenceId));
    }

    public List<Sentence> getSentencesByRules(List<Long> ruleIds) {
        return sentenceRepository.findByRuleIds(ruleIds);
    }

    public List<Sentence> getSentencesByTopicId(Long topicId) {
        return sentenceRepository.findByTopicId(topicId);
    }

    public List<Sentence> getSentencesByTopicIds(List<Long> topicIds) {
        return sentenceRepository.findByTopicIds(topicIds);
    }

    public List<Sentence> getSentencesByTopicsAndRules(List<Long> topicIds, List<Long> ruleIds) {
        return sentenceRepository.findByTopicsAndRules(topicIds, ruleIds);
    }

    public Set<Long> getSentenceIdsByTopicsAndRules(List<Long> topicIds, List<Long> ruleIds) {
        if (topicIds != null && ruleIds == null) {
            // Handle case where only topicIds are provided
            return new HashSet<>(sentenceRepository.findSentenceIdsByTopicIds(topicIds));
        }

        if (topicIds == null && ruleIds != null) {
            // Handle case where only ruleIds are provided
            return new HashSet<>(sentenceRepository.findSentenceIdsByRuleIds(ruleIds));
        }

        // Handle case where both topicIds and ruleIds are provided
        return new HashSet<>(sentenceRepository.findSentenceIdsByTopicsAndRules(topicIds, ruleIds));
    }

    public List<Sentence> searchSentences(Long userId, Set<Long> sentenceIds, List<Long> sentenceTopics) {
        if (userId != null && sentenceIds != null && !sentenceIds.isEmpty() && sentenceTopics != null && !sentenceTopics.isEmpty()) {
            // Search by userId, sentenceIds, and sentenceTopics
            return sentenceRepository.findByUserIdAndSentenceIdInAndSentenceTopics(userId, sentenceIds, sentenceTopics);
        } else if (userId != null && sentenceIds != null && !sentenceIds.isEmpty()) {
            // Search by userId and sentenceIds
            return sentenceRepository.findByUserIdAndSentenceIdIn(userId, sentenceIds);
        } else if (userId != null && sentenceTopics != null && !sentenceTopics.isEmpty()) {
            // Search by userId and sentenceTopics
            return sentenceRepository.findByUserIdAndSentenceTopicsIn(userId, sentenceTopics);

        } else if (sentenceIds != null && !sentenceIds.isEmpty()) {
            // Search by sentenceIds
            return sentenceRepository.findBySentenceIdIn(sentenceIds);
        } else if (sentenceTopics != null && !sentenceTopics.isEmpty()) {
            // Search by sentenceTopics
            return sentenceRepository.findBySentenceTopicsIn(sentenceTopics);
        } else {
            // No search criteria provided, return empty list
            return Collections.emptyList();
        }
    }

    public Page<SentenceDtoTable> getFullSentencesByUserId(Long userId, SortBy sortBy, Sort.Direction direction, String searchWord, int page, int size) {
        // Create the Pageable instance with sorting
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, getSortField(sortBy)));

        // Fetch paged results from the repository
        Page<Sentence> sentencePage = sentenceRepository.findByUserId(userId, pageable);
        List<Sentence> sentences = sentencePage.getContent();

        // Convert to DTOs
        List<SentenceDtoTable> sentenceDtos = new ArrayList<>();
        for (Sentence sentence : sentences) {
            List<TranslationWithRuleDTO> translations = translationService.getTranslationsBySentenceIdWithRules(sentence.getSentenceId());
            List<Topic> topics = topicService.getTopicsBySentenceId(sentence.getSentenceId());
            SentenceDtoTable sentenceDto = new SentenceDtoTable(sentence, translations, topics);
            sentenceDtos.add(sentenceDto);
        }

        // Filter sentences by search word
        if (searchWord != null && !searchWord.isEmpty()) {
            sentenceDtos = sentenceDtos.stream().filter(dto -> containsSearchWord(dto, searchWord)).collect(Collectors.toList());
        }

        // Return paged results
        return new PageImpl<>(sentenceDtos, pageable, sentencePage.getTotalElements());
    }

    private String getSortField(SortBy sortBy) {
        switch (sortBy) {
            case TOPIC_NAME:
                return "topicName"; // Replace with actual field name if necessary
            case RULE_NAME:
                return "ruleName"; // Replace with actual field name if necessary
            case LEARNING_SENTENCE:
                return "learningSentence";
            case CREATE_DATE:
                return "createDate";
            case UPDATE_DATE:
                return "updateDate";
            default:
                return "createDate";
        }
    }

    private boolean containsSearchWord(SentenceDtoTable dto, String searchWord) {
        String lowerCaseSearchWord = searchWord.toLowerCase();

        // Check learningSentence
        if (dto.getLearningSentence().toLowerCase().contains(lowerCaseSearchWord)) {
            return true;
        }

        // Check topics
        for (Topic topic : dto.getTopics()) {
            if (topic.getTopicName().toLowerCase().contains(lowerCaseSearchWord)) {
                return true;
            }
        }

        // Check translations
        for (TranslationWithRuleDTO translation : dto.getTranslations()) {
            if (translation.getTranslation().toLowerCase().contains(lowerCaseSearchWord)) {
                return true;
            }
            // Check rules within translations
            for (RulesAndLinks ruleAndLink : translation.getRulesAndLinks()) {
                if (ruleAndLink.getRule().toLowerCase().contains(lowerCaseSearchWord)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Comparator<SentenceDtoTable> getComparatorForSortBy(SortBy sortBy, Sort.Direction direction) {
        Comparator<SentenceDtoTable> comparator;

        switch (sortBy) {
            case TOPIC_NAME:
                comparator = Comparator.comparing(sentenceDtoTable -> sentenceDtoTable.getTopics().stream()
                        .map(Topic::getTopicName)
                        .sorted()
                        .collect(Collectors.joining(", ")));
                break;
            case RULE_NAME:
                comparator = Comparator.comparing(sentenceDtoTable -> sentenceDtoTable.getTranslations().stream()
                        .flatMap(translation -> translation.getRulesAndLinks().stream())
                        .map(ruleAndLink -> ruleAndLink.getRule()) // Ensure this method exists in RulesAndLinks
                        .sorted()
                        .collect(Collectors.joining(", ")));
                break;
            case LEARNING_SENTENCE:
                comparator = Comparator.comparing(SentenceDtoTable::getLearningSentence);
                break;
            case CREATE_DATE:
                comparator = Comparator.comparing(SentenceDtoTable::getCreateDate, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case UPDATE_DATE:
                comparator = Comparator.comparing(SentenceDtoTable::getUpdateDate, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            default:
                // Default case to sort by createDate if no valid sortBy is provided
                comparator = Comparator.comparing(SentenceDtoTable::getCreateDate, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
        }

        // Reverse order if direction is DESC
        if (direction == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    public SentenceDtoTable getFullSentenceBySentenceId(Long sentenceId) {
        Optional<Sentence> sentenceOpt = sentenceRepository.findById(sentenceId);

        if (sentenceOpt.isPresent()) {
            Sentence sentence = sentenceOpt.get();
            List<TranslationWithRuleDTO> translations = translationService.getTranslationsBySentenceIdWithRules(sentence.getSentenceId());
            List<Topic> topics = topicService.getTopicsBySentenceId(sentence.getSentenceId());
            return new SentenceDtoTable(sentence, translations, topics);
        } else {
            throw new NoSuchElementException("Sentence with id " + sentenceId + " not found");
        }
    }


    @Transactional
    public void updateSentence(Long id, SentenceDTO sentenceDto) {
        Sentence sentence = sentenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sentence not found"));

        // Update basic fields
        sentence.setUserId(sentenceDto.getUserId());
        sentence.setLearningSentence(sentenceDto.getLearningSentence());
        sentence.setComment(sentenceDto.getComment());
        sentence.setUserLink(sentenceDto.getUserLink());

        boolean isRulesUpdated = false;
        boolean isTopicsUpdated = false;

        // Update rules
        if (sentenceDto.getRulesIds() != null) {
            Set<SentenceRule> newRules = sentenceDto.getRulesIds().stream()
                    .map(ruleId -> ruleRepository.findById(ruleId)
                            .orElseThrow(() -> new EntityNotFoundException("Rule not found")))
                    .map(rule -> new SentenceRule(sentence, rule))
                    .collect(Collectors.toSet());

            if (!sentence.getSentenceRules().equals(newRules)) {
                sentence.getSentenceRules().clear();
                sentence.getSentenceRules().addAll(newRules);
                isRulesUpdated = true;
            }
        }

        // Update topics
        if (sentenceDto.getTopicsIds() != null) {
            Set<SentenceTopic> newTopics = sentenceDto.getTopicsIds().stream()
                    .map(topicId -> topicRepository.findById(topicId)
                            .orElseThrow(() -> new EntityNotFoundException("Topic not found")))
                    .map(topic -> new SentenceTopic(sentence, topic))
                    .collect(Collectors.toSet());

            if (!sentence.getSentenceTopics().equals(newTopics)) {
                sentence.getSentenceTopics().clear();
                sentence.getSentenceTopics().addAll(newTopics);
                isTopicsUpdated = true;
            }
        }

        // Update the updateDate if any changes were made
        if (isRulesUpdated || isTopicsUpdated) {
            sentence.setUpdateDate(ZonedDateTime.now());
        }

        sentenceRepository.save(sentence);
    }


    public ResponseEntity<Void> deleteSentenceByIdAndUserId(Long sentenceId, Long userId) {
        Optional<Sentence> sentenceOptional = sentenceRepository.findBySentenceIdAndUserId(sentenceId, userId);
        if (sentenceOptional.isPresent()) {
            sentenceRepository.delete(sentenceOptional.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}