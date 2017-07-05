/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2017 The Catrobat Team
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
package org.catrobat.catroid.common;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.parrot.freeflight.ui.gl.GLBGVideoSprite;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;

public class LegoImageLookData extends LookData {

	private static final String TAG = LegoImageLookData.class.getSimpleName();

	private transient int[] imageSize;
	private transient boolean rgfFormat;

	public LegoImageLookData() {
		rgfFormat = true;
	}

	@Override
	public LegoImageLookData clone() {
		LegoImageLookData cloneImageLookData = new LegoImageLookData();

		cloneImageLookData.name = this.name;
		cloneImageLookData.fileName = this.fileName;
		String filePath = getPathToImageDirectory() + "/" + fileName;
		try {
			ProjectManager.getInstance().getFileChecksumContainer().incrementUsage(filePath);
		} catch (FileNotFoundException fileNotFoundexception) {
			Log.e(TAG, Log.getStackTraceString(fileNotFoundexception));
		}

		return cloneImageLookData;
	}

	@Override
	public int[] getMeasure() {
		return imageSize.clone();
	}

	@Override
	public Pixmap getPixmap() {
		if (pixmap == null) {
			pixmap = Utils.getPixmapFromFile(new File(getAbsolutePath()), true);
		}
		imageSize = new int[] {pixmap.getHeight(), pixmap.getWidth()};
		return pixmap;
	}
}
