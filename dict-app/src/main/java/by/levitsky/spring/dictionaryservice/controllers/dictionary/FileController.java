package by.levitsky.spring.dictionaryservice.controllers.dictionary;

import by.levitsky.spring.dictionaryservice.model.FileInfo;
import by.levitsky.spring.dictionaryservice.service.FileService;
import by.levitsky.spring.dictionaryservice.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/english-words/parse")
public class FileController {
    @Autowired
    FileService fileService;
    @Autowired
    WordService wordService;

//    @GetMapping("")
//    public String homepage() {
//        return "redirect:/files";
//    }

    @GetMapping("/new")
    public String newFile(Model model) {
        return "upload_form";
    }

    @PostMapping("/upload")
    public String uploadFile(Model model, @RequestParam("file") MultipartFile file) {
        String message = "";

        try {
            if(fileService.save(file)){
                wordService.parseWords("uploads/" + file.getOriginalFilename());

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                model.addAttribute("message", message);
            }
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            model.addAttribute("message", message);
        }

        return "redirect:/english-words";
    }

    @GetMapping("/files")
    public String getListFiles(Model model) {
        List<FileInfo> fileInfos = fileService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        model.addAttribute("files", fileInfos);

        return "files";
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = fileService.load(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/files/delete/{filename:.+}")
    public String deleteFile(@PathVariable String filename, Model model, RedirectAttributes redirectAttributes) {
        try {
            boolean existed = fileService.delete(filename);

            if (existed) {
                redirectAttributes.addFlashAttribute("message", "Delete the file successfully: " + filename);
            } else {
                redirectAttributes.addFlashAttribute("message", "The file does not exist!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message",
                    "Could not delete the file: " + filename + ". Error: " + e.getMessage());
        }

        return "redirect:/files";
    }

}
