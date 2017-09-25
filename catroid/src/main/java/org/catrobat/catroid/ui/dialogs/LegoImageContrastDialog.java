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

package org.catrobat.catroid.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.catrobat.catroid.R;

public class LegoImageContrastDialog extends DialogFragment {
	public static final String DIALOG_FRAGMENT_TAG = "dialog_lego_ev3_image_contrast";
	public static final String TAG = LegoImageContrastDialog.class.getSimpleName();
	private int threshold = 0;
	private Bitmap bitmap;
	ImageView imageView;
	TextView textView;

	public LegoImageContrastDialog(Bitmap bitmap, int threshold) {
		this.bitmap = bitmap;
		this.threshold = threshold;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View dialogView = View.inflate(getActivity(), R.layout.dialog_lego_ev3_image_contrast, null);
		imageView = (ImageView) dialogView.findViewById(R.id.image);
		imageView.setImageBitmap(bitmap);
		textView = (TextView) dialogView.findViewById(R.id.label);
		SeekBar seekbar = (SeekBar) dialogView.findViewById(R.id.seekbar);
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
			   imageView.setImageBitmap(changeBitmapContrastBrightness(bitmap, (float) progress / 100f, 1));
			   textView.setText("Contrast: "+(float) progress / 100f);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
	   	});
		Dialog dialog = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.ev3_image_contrast_dialog_title)
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				})
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				})
				.setView(dialogView)
				.create();

		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		return dialog;
	}

	public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness) {
		ColorMatrix cm = new ColorMatrix(new float[]
				{
						contrast, 0, 0, 0, brightness,
						0, contrast, 0, 0, brightness,
						0, 0, contrast, 0, brightness,
						0, 0, 0, 1, 0
				});

		Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

		Canvas canvas = new Canvas(ret);

		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		canvas.drawBitmap(bmp, 0, 0, paint);

		return ret;
	}
}
