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
package org.catrobat.catroid.content.bricks;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.content.Sprite;

import java.util.Collections;
import java.util.List;

public class LegoEv3ShowImageBrick extends SetLookBrick {

	public LegoEv3ShowImageBrick() {
		lego = true;
	}

	@Override
	public int getRequiredResources() {
		return BLUETOOTH_LEGO_EV3;
	}

	@Override
	protected Sprite getSprite() {
		return ProjectManager.getInstance().getCurrentScene().getSpriteList().get(0);
	}

	@Override
	public Brick clone() {
		LegoEv3ShowImageBrick clonedBrick = new LegoEv3ShowImageBrick();
		clonedBrick.setLook(look);
		return clonedBrick;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		convertLookToRgf(look);
		sequence.addAction(sprite.getActionFactory().createLegoEv3ShowImageAction(sprite, look));
		return Collections.emptyList();
	}

	private void convertLookToRgf(LookData look) {
		String absPath = look.getAbsolutePath();
		if(absPath.substring(absPath.lastIndexOf(".")+1, absPath.length()).equals("rgf")) {
			return;
		}
		// TODO: Actual conversion
	}
}
