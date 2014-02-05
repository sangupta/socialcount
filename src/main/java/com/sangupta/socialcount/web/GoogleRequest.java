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

import com.google.gson.FieldNamingPolicy;
import com.sangupta.jerry.util.GsonUtils;

/**
 * Model to generate POST request for Google API call.
 * 
 * @author sangupta
 *
 */
public class GoogleRequest {
	
	public final String method = "pos.plusones.get";
	
	public final String id = "p";
	
	public final String jsonrpc = "2.0";
	
	public final String key = "p";
	
	public final String apiVersion = "v1";
	
	public final Params params;
	
	public GoogleRequest(String url) {
		this.params = new Params(url);
	}
	
	@Override
	public String toString() {
		return GsonUtils.getGson(FieldNamingPolicy.IDENTITY).toJson(this);
	}
	
	public static class Params {
		
		public final boolean nolog = true;
		
		public final String source = "widget";
		
		public final String userId = "viewer";
		
		public final String groupId = "@self";
		
		public final String id;
		
		public Params(String url) {
			this.id = url;
		}
		
	}

}
