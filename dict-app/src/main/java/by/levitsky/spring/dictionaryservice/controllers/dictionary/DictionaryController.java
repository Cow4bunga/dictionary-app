package by.levitsky.spring.dictionaryservice.controllers.dictionary;

import by.levitsky.spring.dictionaryservice.model.EnglishWord;
import by.levitsky.spring.dictionaryservice.service.impl.WordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/english-words")
public class DictionaryController {
    @Autowired
    WordServiceImpl englishWordService;

    @GetMapping("")
    public String getAllWordsPage(Model model) {
        model.addAttribute("english_words", englishWordService.getAllWords());
        return "english-main";
    }


    @GetMapping("/get-regex")
    public String getRegexDialog(Model model){
        EnglishWord word = new EnglishWord();
        word.setFrequency(2.);
        model.addAttribute("english_word", word);
        return "eng/eng-word-regex";
    }

    @GetMapping("/find-by-pattern")
    public String getWordsByRegex(@ModelAttribute("english_word") EnglishWord english_word, Model model){
        model.addAttribute("english_words", englishWordService.getWordByRegex(english_word.getWord()));
        return "english-main";
    }

    @GetMapping("/sorted-alph1")
    public String getSortedByAlph(Model model){
        model.addAttribute("english_words",englishWordService.getSortedByAlphabet());
        return "english-main";
    }

    @GetMapping("/sorted-freq1")
    public String getSortedByFreq(Model model){
        model.addAttribute("english_words",englishWordService.getSortedByFrequency());
        return "english-main";
    }

    @GetMapping("/sorted-alph2")
    public String getSortedByAlphReverse(Model model){
        model.addAttribute("english_words",englishWordService.getSortedInAlphReverse());
        return "english-main";
    }

    @GetMapping("/sorted-freq2")
    public String getSortedByFreqReverse(Model model){
        model.addAttribute("english_words",englishWordService.getSortedByFreqReverse());
        return "english-main";
    }

    @GetMapping("/add")
    public String getAddWordDialog(Model model) {
        EnglishWord word = new EnglishWord();
        //word.setFrequency(0.);
        model.addAttribute("english_word", word);
        return "word-add";
    }

    @PostMapping("")
    public String saveWord(@ModelAttribute("english_word") EnglishWord english_word) {
        englishWordService.addWord(english_word);
        return "redirect:/english-words";
    }

    @GetMapping("/{id}")
    public String getWordByID(@PathVariable(value = "id") long id, Model model) {
        if(englishWordService.getWordById(id).isEmpty()){
            return "redirect:/english-words";
        }

        model.addAttribute("english_word", englishWordService.getWordById(id).get());
        return "english-details";
    }

    @GetMapping("/{id}/edit")
    public String editWord(@PathVariable("id") long id, Model model) {
        if(englishWordService.getWordById(id).isEmpty()){
            return "redirect:/english-words";
        }

        model.addAttribute("english_word", englishWordService.getWordById(id).get());

        return "english-edit";
    }

    @PostMapping("/edit/{id}")
    public String saveEditedWord(@PathVariable("id") long id, @ModelAttribute("english_word") EnglishWord englishWord, Model model){
        englishWordService.updateWord(id,englishWord);
        return "redirect:/english-words";
    }

    @GetMapping("/{id}/remove")
    public String deleteWordConfirmation(@PathVariable("id") long id, Model model){
        if(englishWordService.getWordById(id).isEmpty()){
            return "redirect:/english-words";
        }
        model.addAttribute("english_word", englishWordService.getWordById(id).get());

        return "english-remove";
    }

    @PostMapping("/{id}")
    public String deleteWord(@PathVariable("id") long id, @ModelAttribute("english_word") EnglishWord englishWord, Model model){
        englishWordService.deleteWordById(id);
        return "redirect:/english-words";
    }
}
