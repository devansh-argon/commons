package com.nirav.commons.ads.compose

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.google.android.gms.ads.VideoController
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.nirav.commons.R
import com.nirav.commons.ads.CommonAdManager
import com.nirav.commons.ui.theme.AppColor

@Composable
fun CommonComposeExitDialog(
    onNegativeButtonClick: () -> Unit,
    onPositiveButtonClick: () -> Unit,
    adComposable: @Composable () -> Unit = {
        CommonAdManager.getNativeAd()?.let { NativeAdView(nativeAd = it) }
    }
) {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        Dialog(onDismissRequest = { }) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {

                        adComposable()

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Are you sure want to exit?",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            OutLinedButton(
                                modifier = Modifier.weight(1f),
                                onClick = onNegativeButtonClick
                            )
                            AppButton(
                                modifier = Modifier.weight(1f),
                                primaryColor = AppColor,
                                onClick = onPositiveButtonClick
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun NativeAdView(
    nativeAd: NativeAd
) {
    AndroidView(
        factory = { context ->
            val inflater = LayoutInflater.from(context)
            val adView: NativeAdView =
                inflater.inflate(R.layout.exit_dialog_native_ad, null) as NativeAdView
            val mediaView = adView.findViewById<MediaView>(R.id.mediaView)
            nativeAd.mediaContent?.let { mediaView.mediaContent = it }
            mediaView.setOnHierarchyChangeListener(object :
                ViewGroup.OnHierarchyChangeListener {
                override fun onChildViewAdded(parent: View, child: View) {
                    if (child is ImageView) {
                        child.adjustViewBounds = true
                        child.scaleType = ImageView.ScaleType.FIT_CENTER
                    }
                }

                override fun onChildViewRemoved(parent: View, child: View) {}
            })
            adView.mediaView = mediaView
            adView.headlineView = adView.findViewById(R.id.adTitle)
            adView.bodyView = adView.findViewById(R.id.adDescription)
            adView.iconView = adView.findViewById(R.id.adIcon)
            adView.advertiserView = adView.findViewById(R.id.adAdvertiser)
            adView.callToActionView = adView.findViewById(R.id.callToAction)
            (adView.headlineView as TextView).text = nativeAd.headline
            if (nativeAd.body == null) {
                adView.bodyView?.visibility = View.INVISIBLE
            } else {
                adView.bodyView?.visibility = View.VISIBLE
                (adView.bodyView as TextView).text = "\t\t\t" + nativeAd.body
            }
            if (nativeAd.callToAction == null) {
                adView.callToActionView?.visibility = View.INVISIBLE
            } else {
                adView.callToActionView?.visibility = View.VISIBLE
                (adView.callToActionView as Button).text = nativeAd.callToAction
            }
            if (nativeAd.icon == null) {
                adView.iconView?.visibility = View.GONE
            } else {
                (adView.iconView as ImageView).setImageDrawable(nativeAd.icon?.drawable)
                adView.iconView?.visibility = View.VISIBLE
            }
            adView.mediaView?.visibility = View.VISIBLE
            if (nativeAd.advertiser == null) {
                adView.advertiserView?.visibility = View.INVISIBLE
            } else {
                (adView.advertiserView as TextView).text = nativeAd.advertiser
                adView.advertiserView?.visibility = View.VISIBLE
            }

            val vc = nativeAd.mediaContent?.videoController
            vc?.mute(true)
            if (vc?.hasVideoContent() == true) {
                vc.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {}
            }
            nativeAd.let { adView.setNativeAd(it) }
            return@AndroidView adView
        }
    )
}

@Preview
@Composable
fun PreviewCommonComposeExitDialog() {
    CommonComposeExitDialog(
        onNegativeButtonClick = {},
        onPositiveButtonClick = {}
    )
}

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    primaryColor: Color = AppColor,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(10.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = primaryColor,
            contentColor = Color.White,
            disabledBackgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                .compositeOver(MaterialTheme.colors.surface),
            disabledContentColor = MaterialTheme.colors.onSurface
                .copy(alpha = ContentAlpha.disabled)
        )
    ) {
        Text(
            text = "Yes",
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun OutLinedButton(
    modifier: Modifier = Modifier,
    primaryColor: Color = AppColor,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.primary,
            disabledContentColor = MaterialTheme.colors.onSurface
                .copy(alpha = ContentAlpha.disabled)
        ),
        border = BorderStroke(width = 2.dp, color = primaryColor)
    ) {
        Text(
            text = "No",
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}