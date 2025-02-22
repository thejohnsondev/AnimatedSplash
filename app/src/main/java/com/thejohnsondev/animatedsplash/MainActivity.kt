package com.thejohnsondev.animatedsplash

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.thejohnsondev.animatedsplash.ui.theme.AnimatedSplashTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val BLUR_SCALE_ANIM_START = 0f
private const val BLUR_SCALE_ANIM_END = 1f
private const val BLUR_SCALE_ANIM_DURATION = 1200
private const val BLUR_SCALE_ANIM_DELAY = 500L

private const val LOGO_Y_ANIM_START = 0f
private const val LOGO_Y_ANIM_END_PX = 100f
private const val LOGO_SCALE_ANIM_START = 1.4f
private const val LOGO_SCALE_ANIM_END = 1f

private const val CONTENT_ALPHA_ANIM_DELAY = 200L
private const val CONTENT_ALPHA_ANIM_START = 0f
private const val CONTENT_ALPHA_ANIM_END = 1f

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContent {
            AnimatedSplashTheme {
                WelcomeScreen()
            }
        }
    }
}

@Composable
fun WelcomeScreen() {
    val animatedBackgroundBlurScale = remember {
        Animatable(BLUR_SCALE_ANIM_START)
    }
    val animatedLogoYPosition = remember {
        Animatable(LOGO_Y_ANIM_START)
    }
    val animatedLogoScale = remember {
        Animatable(LOGO_SCALE_ANIM_START)
    }
    val animatedContentAlpha = remember {
        Animatable(CONTENT_ALPHA_ANIM_START)
    }
    val animatedLogoYPosEnd = 0 - with(LocalDensity.current) {
        LOGO_Y_ANIM_END_PX.dp.toPx()
    }

    LaunchedEffect(true) {
        startAnimations(
            this,
            animatedBackgroundBlurScale,
            animatedLogoYPosition,
            animatedLogoYPosEnd,
            animatedLogoScale,
            animatedContentAlpha
        )
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            LogoSection(
                modifier = Modifier.align(Alignment.Center),
                animatedBackgroundBlurScale = animatedBackgroundBlurScale,
                animatedLogoScale = animatedLogoScale,
                animatedLogoYPosition = animatedLogoYPosition
            )
            Titles(animatedContentAlpha)
            ButtonsSection(
                modifier = Modifier.align(Alignment.BottomCenter),
                paddingValues = innerPadding,
                animatedContentAlpha = animatedContentAlpha
            )
        }
    }
}

@Composable
private fun LogoSection(
    modifier: Modifier = Modifier,
    animatedBackgroundBlurScale: Animatable<Float, AnimationVector1D>,
    animatedLogoScale: Animatable<Float, AnimationVector1D>,
    animatedLogoYPosition: Animatable<Float, AnimationVector1D>,
) {
    Box(
        modifier = modifier
    ) {
        Image(modifier = Modifier
            .wrapContentSize()
            .scale(animatedLogoScale.value)
            .graphicsLayer {
                translationY = animatedLogoYPosition.value
            }
            .align(Alignment.Center),
            painter = painterResource(R.drawable.ic_app_logo),
            contentDescription = null)
    }
}


@Composable
private fun Titles(
    animatedContentAlpha: Animatable<Float, AnimationVector1D>,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .alpha(animatedContentAlpha.value),
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .alpha(animatedContentAlpha.value),
            text = stringResource(R.string.app_description),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun ButtonsSection(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    animatedContentAlpha: Animatable<Float, AnimationVector1D>,
) {
    Column(
        modifier = modifier
            .padding(bottom = paddingValues.calculateBottomPadding())
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
    ) {
        Button(modifier = Modifier

            .padding(
                top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp
            )
            .fillMaxWidth()
            .alpha(animatedContentAlpha.value), onClick = {
            // handle click
        }) {
            Text(text = stringResource(R.string.get_started))
        }
    }
}

private suspend fun startAnimations(
    coroutineScope: CoroutineScope,
    animatedBackgroundBlurScale: Animatable<Float, AnimationVector1D>,
    animatedLogoYPosition: Animatable<Float, AnimationVector1D>,
    animatedLogoYPosEnd: Float,
    animatedLogoScale: Animatable<Float, AnimationVector1D>,
    animatedContentAlpha: Animatable<Float, AnimationVector1D>,
) {
    delay(BLUR_SCALE_ANIM_DELAY)
    coroutineScope.launch {
        animatedBackgroundBlurScale.animateTo(
            targetValue = BLUR_SCALE_ANIM_END,
            animationSpec = tween(durationMillis = BLUR_SCALE_ANIM_DURATION)
        )
    }
    coroutineScope.launch {
        animatedLogoYPosition.animateTo(
            targetValue = animatedLogoYPosEnd,
            animationSpec = tween(durationMillis = BLUR_SCALE_ANIM_DURATION)
        )
    }
    coroutineScope.launch {
        animatedLogoScale.animateTo(
            targetValue = LOGO_SCALE_ANIM_END,
            animationSpec = tween(durationMillis = BLUR_SCALE_ANIM_DURATION)
        )
    }
    delay(CONTENT_ALPHA_ANIM_DELAY)
    coroutineScope.launch {
        animatedContentAlpha.animateTo(
            targetValue = CONTENT_ALPHA_ANIM_END,
            animationSpec = tween(durationMillis = BLUR_SCALE_ANIM_DURATION)
        )
    }
}