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
import com.sangupta.jerry.util.UriUtils;

/**
 * Holds the like/comment/share counts for a given URL from various social media
 * sites.
 *
 * @author sangupta
 * 
 */
public class SocialCounts {
	
	/**
	 * The URL for which the details were required
	 */
	public final String url;
	
	/**
	 * The URI encoded format of this URL
	 */
	public final transient String encodedUri;
	
	/**
	 * The time at which these details were collected
	 */
	public final long lastUpdated;
	
	/**
	 * Number of shares on facebook.com
	 */
	public long facebookShares = -1;
	
	/**
	 * Number of likes on facebook.com
	 */
	public long facebookLikes = -1;
	
	/**
	 * Number of comments on facebook.com
	 */
	public long facebookComments = -1;
	
	/**
	 * Number of clicks from facebook.com
	 */
	public long facebookClicks = -1;
	
	/**
	 * Number of tweets on twitter.com
	 */
	public long twitter = -1;
	
	/**
	 * Number of +1s on google.com
	 */
	public long googlePlusOne = -1;
	
	/**
	 * Number of shares on Google Plus
	 */
	public long googleShares = -1;
	
	/**
	 * Number of shares on linkedin.com
	 */
	public long linkedinShares = -1;
	
	/**
	 * Number of pins on pinterest.com
	 */
	public long pinterestPins = -1;
	
	/**
	 * Time taken to fetch all the metrics
	 */
	public long timeTaken;
	
	/**
	 * Convenience constructor. A proper URL is necessary.
	 * 
	 * @throws IllegalArgumentException if the URL is <code>null</code>/empty.
	 * 
	 * @param url The URL for which we are fetching the metrics
	 */
	public SocialCounts(String url) {
		if(AssertUtils.isEmpty(url)) {
			throw new IllegalArgumentException("URL cannot be null/empty");
		}
		
		this.url = url;
		this.encodedUri = UriUtils.encodeURIComponent(url);
		this.lastUpdated = System.currentTimeMillis();
	}

	/**
	 * Mark this complete - basically compute the time taken to fetch
	 * all the metrics. Skipping this method call will only bring in a 
	 * ZERO value for the {@link SocialCounts#timeTaken} attribute.
	 * 
	 */
	public void markComplete() {
		this.timeTaken = System.currentTimeMillis() - this.lastUpdated;
	}
	
	/**
	 * Hash code based on the URL
	 */
	@Override
	public int hashCode() {
		if(this.url == null) {
			return 0;
		}
		
		return this.url.hashCode();
	}
	
	/**
	 * Equality based on the URL
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(obj == this) {
			return true;
		}
		
		if(this.url == null) {
			return false;
		}
		
		if(!(obj instanceof SocialCounts)) {
			return false;
		}
		
		return this.url.equals(((SocialCounts) obj).url);
	}

	/**
	 * Output a human readable state of this object
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(1024);
		
		builder.append("[URL: ");
		builder.append(this.url);
		
		builder.append(", twitter: ");
		builder.append(this.twitter);
		
		builder.append(", facebookShares: ");
		builder.append(this.facebookShares);
		
		builder.append(", facebookComments: ");
		builder.append(this.facebookComments);
		
		builder.append(", facebookLikes: ");
		builder.append(this.facebookLikes);
		
		builder.append(", facebookClicks: ");
		builder.append(this.facebookClicks);
		
		builder.append(", googlePlusOnes: ");
		builder.append(this.googlePlusOne);
		
		builder.append(", googlePlusShares: ");
		builder.append(this.googleShares);
		
		builder.append(", linkedinShares: ");
		builder.append(this.linkedinShares);
		
		builder.append(", pinterestPins: ");
		builder.append(this.pinterestPins);
		
		builder.append(", timeTaken: ");
		builder.append(this.timeTaken);
		
		builder.append(']');
		
		return builder.toString();
	}

}
