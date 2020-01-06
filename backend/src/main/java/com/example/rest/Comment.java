package com.example.rest;

import java.util.*;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Comment {
	private String description = "";
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
