/*

    Adobe AEM Brightcove Connector

    Copyright (C) 2018 Coresecure Inc.

    Authors:    Alessandro Bonfatti
                Yan Kisen
                Pablo Kropilnicki

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

    - Additional permission under GNU GPL version 3 section 7
    If you modify this Program, or any covered work, by linking or combining
    it with httpclient 4.1.3, httpcore 4.1.4, httpmine 4.1.3, jsoup 1.7.2,
    squeakysand-commons and squeakysand-osgi (or a modified version of those
    libraries), containing parts covered by the terms of APACHE LICENSE 2.0
    or MIT License, the licensors of this Program grant you additional
    permission to convey the resulting work.

 */
package com.coresecure.brightcove.wrapper.objects;

import com.coresecure.brightcove.wrapper.enums.PlaylistTypeEnum;
import com.coresecure.brightcove.wrapper.utils.Constants;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <p>Represents a Playlist object to/from the Media API.</p>
 *
 * <p>From the Brightcove documentation on 2009/08/18 (<a href="http://support.brightcove.com/en/docs/media-api-objects-reference#Playlist">http://support.brightcove.com/en/docs/media-api-objects-reference#Playlist</a>):<br/>
 * <code>The Playlist object is a collection of <a href="http://support.brightcove.com/en/docs/media-api-objects-reference#Video">Videos</a>. A Playlist has the following properties::</code>
 * <table>
 * 	<thead>
 * 		<tr>
 * 			<th style="width: 20%;" scope="col"><strong>property name</strong></th>
 * 			<th style="width: 10%;" scope="col"><strong>type</strong></th>
 * 			<th style="width: 10%;" scope="col"><strong>read only?</strong></th>
 * 			<th style="width: 60%;" scope="col"><strong>description</strong></th>
 * 		</tr>
 * 	</thead>
 * 	<tbody>
 * 		<tr>
 * 			<td>id</td>
 * 			<td>long</td>
 * 			<td>yes</td>
 * 			<td>A number that uniquely identifies this Playlist. This id is automatically assigned when the Playlist is created.</td>
 * 		</tr>
 * 		<tr>
 * 			<td>referenceId</td>
 * 			<td>String</td>
 * 			<td>no</td>
 * 			<td>A user-specified id, limited to 150 characters, that uniquely identifies this Playlist. Note that the find_playlists_by_reference_ids method cannot handle referenceIds that contain commas, so you may want to avoid using commas in referenceId values.</td>
 * 		</tr>
 * 		<tr>
 * 			<td>accountId</td>
 * 			<td>long</td>
 * 			<td>yes</td>
 * 			<td>A number that uniquely identifies the account to which this Playlist belongs, assigned by Brightcove.</td>
 * 		</tr>
 * 		<tr>
 * 			<td>name</td>
 * 			<td>String</td>
 * 			<td>no</td>
 * 			<td>The title of this Playlist, limited to 50 characters. The name is a required property when you create a playlist.</td>
 * 		</tr>
 * 		<tr>
 * 			<td>shortDescription</td>
 * 			<td>String</td>
 * 			<td>no</td>
 * 			<td>A short description describing this Playlist, limited to 250 characters.</td>
 * 		</tr>
 * 		<tr>
 * 			<td>videos_ids</td>
 * 			<td>List</td>
 * 			<td>no</td>
 * 			<td>A list of the ids of the <a href="http://support.brightcove.com/en/docs/media-api-objects-reference#Video">Videos</a> that are encapsulated in this Playlist.</td>
 * 		</tr>
 * 		<tr>
 * 			<td>videos</td>
 * 			<td>List</td>
 * 			<td>no</td>
 * 			<td>A list of the <a href="http://support.brightcove.com/en/docs/media-api-objects-reference#Video">Video</a> objects that are encapsulated in this Playlist.</td>
 * 		</tr>
 * 		<tr>
 * 			<td>playlistType</td>
 * 			<td>Enum</td>
 * 			<td>no</td>
 * 			<td>Options are OLDEST_TO_NEWEST, NEWEST_TO_OLDEST, ALPHABETICAL, PLAYSTOTAL, and PLAYS_TRAILING_WEEK (each of which is a smart playlist, ordered as indicated) or EXPLICIT (a manual playlist). The playlistType is a required property when you create a playlist.</td>
 * 		</tr>
 * 		<tr>
 * 			<td>filterTags</td>
 * 			<td>List</td>
 * 			<td>no</td>
 * 			<td>A list of the tags that define this smart playlist. For example:<br/><br/>"filterTags":["Sitka","ticks"]</td>
 * 		</tr>
 * 		<tr>
 * 			<td>thumbnailURL</td>
 * 			<td>String</td>
 * 			<td>yes</td>
 * 			<td>The URL of the thumbnail associated with this Playlist.</td>
 * 		</tr>
 * 	</tbody>
 * </table>
 * </p>
 *
 * @author Sander Gates <three.4.clavins.kitchen @at@ gmail.com>
 *
 */
public class Playlist {
    private String      id;
    private String      created_at;
    private String      updated_at;
    private Boolean     favorite;
    private String      reference_id;
    private String      account_id;
    private String      name;
    private String      description;
    private List<String>   video_ids;
    private PlaylistTypeEnum type;

    /**
     * <p>Default Constructor.</p>
     *
     * <p>All fields set to null to start - required fields must be set before calling Write Media API.</p>
     *
     */
    public Playlist(){/* default */}

    /**
     * <p>Constructor using JSON string.</p>
     *
     * <p>Given a JSON string from the Media API, attempts to construct a new Playlist object and fill out all of the fields defined.  All other fields will be null.</p>
     *
     */
    public Playlist(String json) throws JSONException {
        if(json == null){
            throw new JSONException("[ERR] Playlist can not be parsed from null JSON string.");
        }

        JSONObject jsonObj = new JSONObject(json);

        finishConstruction(jsonObj);
    }

    /**
     * <p>Constructor using JSON object.</p>
     *
     * <p>Given a JSON object from the Media API, attempts to construct a new Playlist object and fill out all of the fields defined.  All other fields will be null.</p>
     *
     */
    public Playlist(JSONObject jsonObj) throws JSONException {
        finishConstruction(jsonObj);
    }

    /**
     * <p>Private method to finish construction for other constructors</p>
     *
     * @param jsonObj
     * @throws JSONException
     */
    private void finishConstruction(JSONObject jsonObj) throws JSONException {
        Iterator<String> rootKeys = jsonObj.keys();
        if(jsonObj.has(Constants.NAME)) name = jsonObj.getString(Constants.NAME);
        if(jsonObj.has(Constants.ID)) id = jsonObj.getString(Constants.ID);
        if(jsonObj.has(Constants.FAVORITE)) favorite = jsonObj.getBoolean(Constants.FAVORITE);
        if(jsonObj.has(Constants.ACCOUNT_ID)) account_id = jsonObj.getString(Constants.ACCOUNT_ID);
        if(jsonObj.has(Constants.CREATED_AT)) created_at = jsonObj.getString(Constants.CREATED_AT);
        if(jsonObj.has(Constants.UPDATED_AT)) updated_at = jsonObj.getString(Constants.UPDATED_AT);
        if(jsonObj.has(Constants.REFERENCE_ID)) reference_id = jsonObj.getString(Constants.REFERENCE_ID);
        if(jsonObj.has(Constants.DESCRIPTION)) description = jsonObj.getString(Constants.DESCRIPTION);
        if(jsonObj.has(Constants.VIDEO_IDS)) {
            video_ids = new ArrayList<String>();
            JSONArray idsArray = jsonObj.getJSONArray(Constants.VIDEO_IDS);
            for(int idIdx=0;idIdx<idsArray.length();idIdx++){
                video_ids.add(idsArray.getString(idIdx));
            }
        }
        if(jsonObj.has(Constants.PLAYLIST_TYPE)) type = getType(jsonObj.getString(Constants.PLAYLIST_TYPE));
    }

    private PlaylistTypeEnum getType(String value) throws JSONException{
        PlaylistTypeEnum type = null;
        if(value.equals("OLDEST_TO_NEWEST")){
            type = PlaylistTypeEnum.OLDEST_TO_NEWEST;
        }
        else if(value.equals("NEWEST_TO_OLDEST")){
            type = PlaylistTypeEnum.NEWEST_TO_OLDEST;
        }
        else if(value.equals("ALPHABETICAL")){
            type = PlaylistTypeEnum.ALPHABETICAL;
        }
        else if(value.equals("PLAYSTOTAL")){
            type = PlaylistTypeEnum.PLAYSTOTAL;
        }
        else if(value.equals("PLAYS_TRAILING_WEEK")){
            type = PlaylistTypeEnum.PLAYS_TRAILING_WEEK;
        }
        else if(value.equals("EXPLICIT")){
            type = PlaylistTypeEnum.EXPLICIT;
        }
        else{
            throw new JSONException("[ERR] Media API specified invalid value for playlist type '" + value + "'.  Acceptable values are 'OLDEST_TO_NEWEST', 'NEWEST_TO_OLDEST', 'ALPHABETICAL', 'PLAYSTOTAL', 'PLAYS_TRAILING_WEEK', 'EXPLICIT'.");
        }
        return type;
    }

    /**
     * <p>Gets the id for this Playlist.</p>
     *
     * <p><code>A number that uniquely identifies this Playlist. This id is automatically assigned when the Playlist is created.</code></p>
     *
     * @return The id for this Playlist
     */
    public String getId(){
        return id;
    }

    /**
     * <p>Gets the reference id for this Playlist.</p>
     *
     * <p><code>A user-specified id, limited to 150 characters, that uniquely identifies this Playlist. Note that the find_playlists_by_reference_ids method cannot handle referenceIds that contain commas, so you may want to avoid using commas in referenceId values.</code></p>
     *
     * @return Reference id for this Playlist
     */
    public String getReferenceId(){
        return reference_id;
    }

    /**
     * <p>Gets the account id for this Playlist.</p>
     *
     * <p><code>A number that uniquely identifies the account to which this Playlist belongs, assigned by Brightcove.</code></p>
     *
     * @return Account id for this Playlist
     */
    public String getAccountId(){
        return account_id;
    }

    /**
     * <p>Gets the name (title) for this Playlist.</p>
     *
     * <p><code>The title of this Playlist, limited to 50 characters. The name is a required property when you create a playlist.</code></p>
     *
     * @return Name of the Playlist
     */
    public String getName(){
        return name;
    }

    /**
     * <p>Gets the short description for this Playlist.</p>
     *
     * <p><code>A short description describing this Playlist, limited to 250 characters.</code></p>
     *
     * @return Short description for this Playlist
     */
    public String getDescription(){
        return description;
    }

    /**
     * <p>Gets the Video Ids for this Playlist.</p>
     *
     * <p><code>A list of the ids of the <a href="http://support.brightcove.com/en/docs/media-api-objects-reference#Video">Videos</a> that are encapsulated in this Playlist.</code></p>
     *
     * @return Video Ids for this Playlist
     */
    public List<String> getVideoIds(){
        return video_ids;
    }


    /**
     * <p>Gets the type for this Playlist.</p>
     *
     * <p><code>Options are OLDEST_TO_NEWEST, NEWEST_TO_OLDEST, ALPHABETICAL, PLAYSTOTAL, and PLAYS_TRAILING_WEEK (each of which is a smart playlist, ordered as indicated) or EXPLICIT (a manual playlist). The playlistType is a required property when you create a playlist.</code></p>
     *
     * @return Type for this Playlist
     */
    public PlaylistTypeEnum getPlaylistType(){
        return type;
    }


    /**
     * <p>Sets the id for this Playlist.</p>
     *
     * <p><code>A number that uniquely identifies this Playlist. This id is automatically assigned when the Playlist is created.</code></p>
     *
     * @param id The id for this Playlist
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * <p>Sets the reference id for this Playlist.</p>
     *
     * <p><code>A user-specified id, limited to 150 characters, that uniquely identifies this Playlist. Note that the find_playlists_by_reference_ids method cannot handle referenceIds that contain commas, so you may want to avoid using commas in referenceId values.</code></p>
     *
     * @param referenceId Reference id for this Playlist
     */
    public void setReferenceId(String referenceId){
        this.reference_id = referenceId;
    }

    /**
     * <p>Sets the account id for this Playlist.</p>
     *
     * <p><code>A number that uniquely identifies the account to which this Playlist belongs, assigned by Brightcove.</code></p>
     *
     * @param account_id Account id for this Playlist
     */
    public void setAccountId(String account_id){
        this.account_id = account_id;
    }

    /**
     * <p>Sets the name (title) for this Playlist.</p>
     *
     * <p><code>The title of this Playlist, limited to 50 characters. The name is a required property when you create a playlist.</code></p>
     *
     * @param name Name of the Playlist
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * <p>Sets the short description for this Playlist.</p>
     *
     * <p><code>A short description describing this Playlist, limited to 250 characters.</code></p>
     *
     * @param shortDescription Short description for this Playlist
     */
    public void setDescription(String shortDescription){
        this.description = shortDescription;
    }

    /**
     * <p>Sets the Video Ids for this Playlist.</p>
     *
     * <p><code>A list of the ids of the <a href="http://support.brightcove.com/en/docs/media-api-objects-reference#Video">Videos</a> that are encapsulated in this Playlist.</code></p>
     *
     * @param video_ids Video Ids for this Playlist
     */
    public void setVideoIds(List<String> video_ids){
        this.video_ids = video_ids;
    }

    /**
     * <p>Sets the type for this Playlist.</p>
     *
     * <p><code>Options are OLDEST_TO_NEWEST, NEWEST_TO_OLDEST, ALPHABETICAL, PLAYSTOTAL, and PLAYS_TRAILING_WEEK (each of which is a smart playlist, ordered as indicated) or EXPLICIT (a manual playlist). The playlistType is a required property when you create a playlist.</code></p>
     *
     * @param playlistType Type for this Playlist
     */
    public void setPlaylistType(PlaylistTypeEnum playlistType){
        this.type = playlistType;
    }

    /**
     * <p>Converts the video into a JSON object suitable for use with the Media API</p>
     *
     * @return JSON object representing the video
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();

        if(name != null){
            json.put("name", name);
        }
        if(id != null){
            json.put("id", id);
        }
        if(reference_id != null){
            json.put("reference_id", reference_id);
        }
        if(account_id != null){
            json.put("account_id", account_id);
        }
        if(favorite != null){
            json.put("favorite", favorite);
        }
        if(created_at != null){
            json.put("created_at", created_at);
        }
        if(updated_at != null){
            json.put("updated_at", updated_at);
        }

        if(description != null){
            json.put("description", description);
        }
        if(video_ids != null){
            JSONArray idArray = new JSONArray();
            for(String videoId : video_ids){
                idArray.put(videoId);
            }
            json.put("video_ids", idArray);
        }

        if(type != null){
            json.put("type", type);
        }

        return json;
    }

}