/**
 *
 * socialcounts - find social media strength of a URL
 * Copyright (c) 2014, Sandeep Gupta
 * 
 * http://sangupta.com/projects/socialcounts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.sangupta.socialcount;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.ConsoleUtils;

/**
 * A simple command line client to find and display
 * the social media strength of a given URL.
 * 
 * @author sangupta
 *
 */
public class SocialCountClientMain {

	/**
	 * Command line client for {@link SocialCountClient}.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Running SocialCountsClient v1.0, Copyright (C) 2014 Sandeep Gupta\n");
		
		if(args.length == 0) {
			do {
				String url = ConsoleUtils.readLine("Enter the URL (empty to exit): ", true);
				if(AssertUtils.isBlank(url)) {
					System.out.println("Nothing to do... exiting!");
					return;
				}
				
				displayStats(url);
			} while(true);
		}
		
		// display stats for every URL on command line
		for(String url : args) {
			displayStats(url);
		}
	}

	/**
	 * Display stats for the given URL.
	 * 
	 * @param url
	 */
	private static void displayStats(String url) {
		SocialCounts counts = SocialCountClient.getSocialCounts(url);
		if(counts == null) {
			System.out.println("Something went wrong while fetching metrics!");
			return;
		}
		
		System.out.println("URL provided: " + counts.url);
		System.out.println("Tweets: " + counts.twitter);
		System.out.println("Facebook likes: " + counts.facebookLikes + ", comments: " + counts.facebookComments + ", shares: " + counts.facebookShares + ", clicks: " + counts.facebookClicks);
		System.out.println("Google +1: " + counts.googlePlusOne + ", shares: " + counts.googleShares);
		System.out.println("Linkedin shares: " + counts.linkedinShares);
		System.out.println("Pinterest pins: " + counts.pinterestPins);
		System.out.println("Time taken: " + counts.timeTaken);
		System.out.println();
	}
}
