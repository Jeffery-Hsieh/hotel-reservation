package com.example.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Comment {
	@XmlElement
	private String description = "";
	@XmlElement
	private int starRate = 0;
		
	public
		Comment(){}

		Comment(String description, int starRate) {
			this.description = description;
			this.starRate = starRate;
		}

		String GetDescription() {
			return this.description;
		}
		
		int GetStarRate() {
			return this.starRate;
		}

}
