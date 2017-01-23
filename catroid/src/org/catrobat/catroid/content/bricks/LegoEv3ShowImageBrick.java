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
package org.catrobat.catroid.content.bricks;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.ui.ScriptActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LegoEv3ShowImageBrick extends BrickBaseType {
	private static final long serialVersionUID = 1L;
	private String fileName;
	private transient String oldFileName;
	private List<String> fileNameList;
	private transient AdapterView<?> adapterView;
	public transient boolean isBackpackLookData = false;

	public LegoEv3ShowImageBrick() {
	}

	public void setImage(String fileName) {
		this.fileName = fileName;
	}

	public String getImage() {
		return this.fileName;
	}

//	private ArrayList<String> findRGFs(File dir, ArrayList<String> matchingRGFFileNames) {
//		if (dir == null) {
//			dir = new File(Environment.getExternalStorageDirectory().toString());
//		}
//		String rgfPattern = ".rgf";
//
//		File listFile[] = dir.listFiles();
//
//		if (listFile != null) {
//			for (int i = 0; i < listFile.length; i++) {
//
//				if (listFile[i].isDirectory()) {
//					findRGFs(listFile[i], matchingRGFFileNames);
//				} else {
//					if (listFile[i].getName().endsWith(rgfPattern)){
//						matchingRGFFileNames.add(dir.toString() + File.separator + listFile[i].getName());
//						Log.d("ALT","Found one! " + dir.toString() + listFile[i].getName());
//					}
//				}
//			}
//		}
//		Log.d("ALT", "Outgoing size: " + matchingRGFFileNames.size());
//		return matchingRGFFileNames;
//	}

	private ArrayAdapter<String> createImageAdapter(Context context) {
		ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		String newImage = context.getString(R.string.new_broadcast_message);
		arrayAdapter.add(newImage);
		return arrayAdapter;
	}

	private void setSpinnerSelection(Spinner spinner) {
		if (fileNameList != null && fileNameList.contains(fileName)) {
			oldFileName = fileName;
			spinner.setSelection(fileNameList.indexOf(fileName) + 1, true);
		} else {
			if (spinner.getAdapter() != null && spinner.getAdapter().getCount() > 1) {
				if (fileNameList.indexOf(oldFileName) >= 0) {
					spinner.setSelection(fileNameList.indexOf(oldFileName) + 1, true);
				} else {
					spinner.setSelection(1, true);
				}
			} else {
				spinner.setSelection(0, true);
			}
		}
	}

	@Override
	public int getRequiredResources() {
		return BLUETOOTH_LEGO_EV3;
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		LegoEv3ShowImageBrick copyBrick = (LegoEv3ShowImageBrick) clone();

		if (fileName != null && isBackpackLookData) { // isBackpackLookData belongs to file management class
			copyBrick.fileName = fileName; //.clone();
			copyBrick.isBackpackLookData = false;
			return copyBrick;
		}

		for (String data : fileNameList) {
			if (fileName != null && data != null) { // && data.getAbsolutePath().equals(filename.getAbsolutePath())) {
				copyBrick.fileName = data; //.clone();
				copyBrick.isBackpackLookData = true; // ---.sound.---
				break;
			}
		}
		return copyBrick;
	}

	//public String getImagePath() { return image.getAbsolutePath(); }

	@Override
	public View getPrototypeView(Context context) {
		View prototypeView = View.inflate(context, R.layout.brick_ev3_show_image, null);
		Spinner showImageSpinner = (Spinner) prototypeView.findViewById(R.id.ev3_show_image_spinner);
		showImageSpinner.setFocusableInTouchMode(false);
		showImageSpinner.setFocusable(false);
		showImageSpinner.setEnabled(false);

		SpinnerAdapter showImageSpinnerAdapter = createImageAdapter(context);
		showImageSpinner.setAdapter(showImageSpinnerAdapter);
		setSpinnerSelection(showImageSpinner);

//		// When replaced delete ev3_show_image_chooser in array.xml
//		File dir = new File(Environment.getExternalStorageDirectory().toString());
//		ArrayList<String> imageFileNames = new ArrayList<String>();
//		imageFileNames = findRGFs(dir, imageFileNames);
//		ArrayAdapter<String> imageAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, imageFileNames);
//		imageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//		showImageSpinner.setAdapter(imageAdapter);
//		//showImageSpinner.setSelection(imageEnum.ordinal());
		return prototypeView;
	}

	@Override
	public Brick clone() {
		LegoEv3ShowImageBrick clonedBrick = new LegoEv3ShowImageBrick();
		clonedBrick.setImage(fileName);
		return clonedBrick;
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}
		if (view == null) {
			alphaValue = 255;
		}
		view = View.inflate(context, R.layout.brick_ev3_show_image, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(R.id.brick_ev3_show_image_checkbox);
		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		final Spinner imageSpinner = (Spinner) view.findViewById(R.id.ev3_show_image_spinner);

		if (!(checkbox.getVisibility() == View.VISIBLE)) {
			imageSpinner.setClickable(true);
			imageSpinner.setEnabled(true);
		} else {
			imageSpinner.setClickable(false);
			imageSpinner.setEnabled(false);
		}

		final ArrayAdapter<String> spinnerAdapter = createImageAdapter(context);

		SpinnerAdapterWrapper spinnerAdapterWrapper = new SpinnerAdapterWrapper(context, spinnerAdapter);

		imageSpinner.setAdapter(spinnerAdapterWrapper);

		imageSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
					fileName = "mindstorms.rgf";
				} else {
					fileName = parent.getItemAtPosition(position).toString();
					oldFileName = fileName;
					adapterView = parent;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		setSpinnerSelection(imageSpinner);

//		ArrayList<String> imageFileNames = new ArrayList<String>();
//		imageFileNames = findRGFs(dir, imageFileNames);
//		ArrayAdapter<CharSequence> imageAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, imageFileNames);
//		imageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//		imageSpinner.setAdapter(imageAdapter);
//		//imageSpinner.setSelection(imageEnum.ordinal());
		return view;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(R.id.brick_ev3_show_image_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView textLegoShowImageLabel = (TextView) view.findViewById(R.id.ev3_show_image_text_view);
			textLegoShowImageLabel.setTextColor(textLegoShowImageLabel.getTextColors().withAlpha(alphaValue));

			Spinner imageSpinner = (Spinner) view.findViewById(R.id.ev3_show_image_spinner);
			ColorStateList color = textLegoShowImageLabel.getTextColors().withAlpha(alphaValue);
			imageSpinner.getBackground().setAlpha(alphaValue);
			if (adapterView != null) {
				((TextView) adapterView.getChildAt(0)).setTextColor(color);
			}

			this.alphaValue = alphaValue;
		}

		return view;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createLegoEv3ShowImageAction(fileName));
		return null;
	}

//	private void setOnLookDataListChangedAfterNewListener(Context context) {
//		ScriptActivity scriptActivity = (ScriptActivity) context;
//		LookFragment lookFragment = (LookFragment) scriptActivity.getFragment(ScriptActivity.FRAGMENT_LOOKS);
//		if (lookFragment != null) {
//			lookFragment.setOnLookDataListChangedAfterNewListener(this);
//		}
//	}

	private class SpinnerAdapterWrapper implements SpinnerAdapter {

		protected Context context;
		protected ArrayAdapter<String> spinnerAdapter;

		private boolean isTouchInDropDownView;

		public SpinnerAdapterWrapper(Context context, ArrayAdapter<String> spinnerAdapter) {
			this.context = context;
			this.spinnerAdapter = spinnerAdapter;

			this.isTouchInDropDownView = false;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver paramDataSetObserver) {
			spinnerAdapter.registerDataSetObserver(paramDataSetObserver);
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver) {
			spinnerAdapter.unregisterDataSetObserver(paramDataSetObserver);
		}

		@Override
		public int getCount() {
			return spinnerAdapter.getCount();
		}

		@Override
		public Object getItem(int paramInt) {
			return spinnerAdapter.getItem(paramInt);
		}

		@Override
		public long getItemId(int paramInt) {
			String currentImage = spinnerAdapter.getItem(paramInt);
			if (!currentImage.equals(context.getString(R.string.new_broadcast_message))) {
				//currentImage.getLookName().equals(context.getString(R.string.new_broadcast_message))) {
				oldFileName = currentImage;
			}
			return spinnerAdapter.getItemId(paramInt);
		}

		@Override
		public boolean hasStableIds() {
			return spinnerAdapter.hasStableIds();
		}

		@Override
		public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
			if (isTouchInDropDownView) {
				isTouchInDropDownView = false;
//				if (paramInt == 0) {
//					switchToLookFragmentFromScriptFragment();
//				}
			}
			return spinnerAdapter.getView(paramInt, paramView, paramViewGroup);
		}

		@Override
		public int getItemViewType(int paramInt) {
			return spinnerAdapter.getItemViewType(paramInt);
		}

		@Override
		public int getViewTypeCount() {
			return spinnerAdapter.getViewTypeCount();
		}

		@Override
		public boolean isEmpty() {
			return spinnerAdapter.isEmpty();
		}

		@Override
		public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup) {
			View dropDownView = spinnerAdapter.getDropDownView(paramInt, paramView, paramViewGroup);

			dropDownView.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
					isTouchInDropDownView = true;
					return false;
				}
			});

			return dropDownView;
		}

		private void switchToLookFragmentFromScriptFragment() {
			ScriptActivity scriptActivity = ((ScriptActivity) context);
			scriptActivity.switchToFragmentFromScriptFragment(ScriptActivity.FRAGMENT_LOOKS);

			//setOnLookDataListChangedAfterNewListener(context);
		}
	}

//	@Override
//	public void onImageInfoListChangedAfterNew(String fileNameInfo) {
//		fileName = fileNameInfo;
//		oldFileName = fileNameInfo;
//	}
//
//	@Override
//	public void storeDataForBackPack(Sprite sprite) {
//		if (fileName == null) {
//			return;
//		}
//		fileName = SoundController.getInstance().backPackHiddenSound(sound);
//		if (sprite != null && !sprite.getSoundList().contains(sound)) {
//			sprite.getSoundList().add(sound);
//		}
//	}
}
