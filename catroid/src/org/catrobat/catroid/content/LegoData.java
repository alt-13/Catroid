/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2016 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.content;

import android.graphics.Bitmap;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.utils.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

public class LegoData implements Serializable, Comparable<LegoData>, Cloneable {

	private static final long serialVersionUID = 1L;
	private static final String TAG = LegoData.class.getSimpleName();

	// For Soundfiles
	public transient boolean isPlaying;
	// For Picture // TODO: protected/private?
	protected transient Bitmap thumbnailBitmap;
	protected transient Integer width;
	protected transient Integer height;
	protected static final transient int THUMBNAIL_WIDTH = 150;
	protected static final transient int THUMBNAIL_HEIGHT = 150;
	protected transient Pixmap pixmap = null;
	protected transient Pixmap originalPixmap = null;
	protected transient TextureRegion textureRegion = null;

	private String name;
	private String fileName;
	private FileType fileType;
	public transient boolean isBackpackLegoData;

	public enum FileType {
		SOUND, IMAGE
	}

	public LegoData(FileType file) {
		isBackpackLegoData = false;
		fileType = file;
	}

	// For Picture
	public void draw(Batch batch, float alpha) {
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LegoData)) {
			return false;
		}
		if (obj == this) {
			return true;
		}

		LegoData legoData = (LegoData) obj;
		if (legoData.fileName.equals(this.fileName) && legoData.name.equals(this.name)) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode() + fileName.hashCode() + super.hashCode();
	}

	@Override
	public LegoData clone() {
		LegoData cloneLegoData = new LegoData(fileType);

		cloneLegoData.name = this.name;
		cloneLegoData.fileName = this.fileName;
		cloneLegoData.isBackpackLegoData = false;
		String filePath;
		if (fileType == FileType.IMAGE) {
			filePath = getPathToImageDirectory() + "/" + fileName;
		} else {
			filePath = getPathToSoundDirectory() + "/" + fileName;
		}

		try {
			ProjectManager.getInstance().getFileChecksumContainer().incrementUsage(filePath);
		} catch (FileNotFoundException fileNotFoundexception) {
			Log.e(TAG, Log.getStackTraceString(fileNotFoundexception));
		}

		return cloneLegoData;
	}

//	public LegoData copySoundInfoForSprite(Sprite sprite) {
//		LegoData cloneSoundInfo = new LegoData();
//
//		cloneSoundInfo.name = this.name;
//
//		try {
//			cloneSoundInfo.fileName = StorageHandler
//					.getInstance()
//					.copySoundFile(
//							Utils.buildPath(
//									Utils.buildProjectPath(ProjectManager.getInstance().getCurrentProject().getName()),
//									Constants.SOUND_DIRECTORY, fileName)).getName();
//		} catch (IOException ioException) {
//			Log.e(TAG, Log.getStackTraceString(ioException));
//		}
//
//		return cloneSoundInfo;
//	}

	public void resetLookData() {
		pixmap = null;
		originalPixmap = null;
		textureRegion = null;
	}

	public TextureRegion getTextureRegion() {
		if (textureRegion == null) {
			textureRegion = new TextureRegion(new Texture(getPixmap()));
		}
		return textureRegion;
	}

	public Pixmap getPixmap() {
		if (pixmap == null) {
			try {
				pixmap = new Pixmap(Gdx.files.absolute(getAbsolutePath()));
			} catch (GdxRuntimeException gdxRuntimeException) {
				Log.e(TAG, "gdx.files throws GdxRuntimeException", gdxRuntimeException);
				if (gdxRuntimeException.getMessage().startsWith("Couldn't load file:")) {
					pixmap = new Pixmap(1, 1, Pixmap.Format.Alpha);
				}
			} catch (NullPointerException nullPointerException) {
				Log.e(TAG, "gdx.files throws NullPointerException", nullPointerException);
			}
		}
		return pixmap;
	}

	public void setPixmap(Pixmap pixmap) {
		this.pixmap = pixmap;
	}

	public String getAbsolutePath() {
		if (fileName != null) {
			if (isBackpackLegoData) {
				return Utils.buildPath(getPathToBackPackSoundDirectory(), fileName);
			} else {
				return Utils.buildPath(getPathToSoundDirectory(), fileName);
			}
		} else {
			return null;
		}
	}

	public String getAbsoluteProjectPath() {
		if (fileName != null) {
			return Utils.buildPath(getPathToSoundDirectory(), fileName);
		} else {
			return null;
		}
	}

	public String getAbsoluteBackPackPath() {
		if (fileName != null) {
			return Utils.buildPath(getPathToBackPackSoundDirectory(), fileName);
		} else {
			return null;
		}
	}

	public String getTitle() {
		return name;
	}

	public void setTitle(String title) {
		this.name = title;
	}

	public String getSoundFileName() {
		return fileName;
	}

	public void setSoundFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getChecksum() {
		if (fileName == null) {
			return null;
		}
		return fileName.substring(0, 32);
	}

	protected String getPathToImageDirectory() { // TODO: protected?
		return Utils.buildPath(Utils.buildProjectPath(ProjectManager.getInstance().getCurrentProject().getName()),
				Constants.IMAGE_DIRECTORY);
	}

	private String getPathToSoundDirectory() {
		return Utils.buildPath(Utils.buildProjectPath(ProjectManager.getInstance().getCurrentProject().getName()),
				Constants.SOUND_DIRECTORY);
	}

	private String getPathToBackPackSoundDirectory() {
		return Utils.buildPath(Constants.DEFAULT_ROOT, Constants.BACKPACK_DIRECTORY,
				Constants.BACKPACK_SOUND_DIRECTORY);
	}

	@Override
	public int compareTo(LegoData soundInfo) {
		return name.compareTo(soundInfo.name);
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean isBackpackSoundInfo() {
		return isBackpackSoundInfo;
	}
}
