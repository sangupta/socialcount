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

package com.sangupta.socialcount.web;

/**
 * Model to parse JSON response from Facebook API call.
 * 
 * @author sangupta
 *
 */
public class FacebookResponse {
	
	public Data[] data;
	
	public static class Data {
		
		public String url;
		
		public long likeCount;
		
		public long clickCount;
		
		public long shareCount;
		
		public long commentCount;
	
	}

}