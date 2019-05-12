package com.github.antonhand.bibreco.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import com.github.antonhand.bibreco.model.*;
import com.github.antonhand.bibreco.Process;
import com.github.antonhand.bibreco.features.*;

@Controller
public class BibrecoController {
	
	String[] featureNames = {"Тематическая близость", "Встречаемость источника", "Встречаемость авторов",
							 "Встречаемость журнала", "Новизна публикации"};
	
	String filePath = System.getProperty("catalina.home") + "/work/bibreco/";

    
    @GetMapping("/bibreco")
    public String diplomaForm(Model model) {
    	
        model.addAttribute("output", new BibrecoForm(featureNames.length));
        model.addAttribute("input", featureNames);
        return "index";
    }
    
    
    @PostMapping("/bibreco")
    public String diplomaSubmit(@ModelAttribute BibrecoForm output, Model model) throws Exception { 
    	File dir = new File(filePath);
    	if(!dir.exists()) {
    		dir.mkdir();
    	}
    	for (File fl: new File(filePath).listFiles()) 
            if (fl.isFile()) fl.delete();
    	
    	List<String> fileNames = null;
    	  
	    fileNames = new ArrayList<String>(); 
	    for(MultipartFile mf : output.getFiles()) { 
	    	mf.transferTo(new File( filePath + mf.getOriginalFilename()));
	        fileNames.add(mf.getOriginalFilename());
	    }
	    
	    Process pr = new Process();
        List<Bibliography> b = pr.bibliographiesFromPdfs(filePath);
        List f = new ArrayList<Feature>();
        
        int[] weights = output.getWeights();
        
        f.add(new TopicModel(50, weights[0]));
        f.add(new BibliographiesCount(weights[1]));
        f.add(new AuthorsRefsCount(weights[2]));
        f.add(new JournalRefsCount(weights[3]));
        f.add(new Newness(weights[4]));
        Set<Record> s = pr.rank(b, f);
	    
	    model.addAttribute("records", s);
	    model.addAttribute("files", String.join(", ", fileNames));
    	
	    for (File fl: new File(filePath).listFiles()) 
            if (fl.isFile()) fl.delete();
    	  
    	return "upload";
    }

}
