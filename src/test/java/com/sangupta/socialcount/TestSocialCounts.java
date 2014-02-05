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

import org.junit.Test;
import org.springframework.util.Assert;

/**
 * Test suite for the {@link SocialCountClient} and also
 * some rough test around performance of parallel client than 
 * the one by one client.
 * 
 * @author sangupta
 *
 */
public class TestSocialCounts {
	
	@Test
	public void testAllSocialAccounts() {
		SocialCounts counts = SocialCountClient.getSocialCountsParallel("http://facebook.com");
		Assert.isTrue(counts.twitter > 0);
		
		Assert.isTrue(counts.facebookClicks >= 0);
		Assert.isTrue(counts.facebookComments >= 0);
		Assert.isTrue(counts.facebookLikes >= 0);
		Assert.isTrue(counts.facebookShares >= 0);
		
		Assert.isTrue(counts.googlePlusOne >= 0);
		Assert.isTrue(counts.googleShares >= 0);
		
		Assert.isTrue(counts.linkedinShares >= 0);
		
		Assert.isTrue(counts.pinterestPins >= 0);
	}
	
	/**
	 * Simple method to show various stats for facebook.com as a command
	 * line app.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "http://facebook.com";
		SocialCounts counts1 = SocialCountClient.getSocialCounts(url);
		System.out.println(counts1);
		
		SocialCounts counts2 = SocialCountClient.getSocialCountsParallel(url);
		System.out.println(counts2);
	}
	
}
