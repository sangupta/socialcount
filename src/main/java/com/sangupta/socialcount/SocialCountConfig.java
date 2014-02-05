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

/**
 * A simple configuration file that tells which all providers
 * need to be run for the given URL. This helps reduce the load
 * when we are interested only in a group of providers and not
 * all the ones.
 * 
 * @author sangupta
 *
 */
public class SocialCountConfig {
	
	/**
	 * Whether to hit Twitter API or not
	 */
	public boolean twitter = true;
	
	/**
	 * Whether to hit Facebook API or not
	 */
	public boolean facebook = true;
	
	/**
	 * Whether to hit Google Plus One API or not
	 */
	public boolean googlePlusOne = true;
	
	/**
	 * Whether to hit Google Plus shares API or not
	 */
	public boolean googleShares = true;
	
	/**
	 * Whether to hit LinkedIn API or not
	 */
	public boolean linkedin = true;
	
	/**
	 * Whether to hit Pinterest API or not
	 */
	public boolean pinterest = true;

}
