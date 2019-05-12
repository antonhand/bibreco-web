package com.github.antonhand.bibreco.web;

import org.springframework.web.multipart.MultipartFile;

public class BibrecoForm {

	private int[] weights;
	private MultipartFile[] files;
	
	public BibrecoForm() {
		super();
	}
	
	public BibrecoForm(int featureNum) {
		super();
		this.weights = new int[featureNum];
	}

	public int[] getWeights() {
		return weights;
	}

	public void setWeights(int[] weights) {
		this.weights = weights;
	}

	public MultipartFile[] getFiles() {
		return files;
	}

	public void setFiles(MultipartFile[] files) {
		this.files = files;
	}

}
