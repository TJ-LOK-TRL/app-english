package com.masterproject.englishapp.legal

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

object LegalTexts {

    val PRIVACY_POLICY = buildAnnotatedString {

        withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
            append("Effective Date: December 19, 2024\n")
        }

        append("\n\n")

        append(
            "Firelingo (\"we\", \"our\", or \"us\") is committed to protecting your privacy. " +
                    "This Privacy Policy explains how we collect, use, store, and protect your personal " +
                    "information when you use the Firelingo mobile application and related services (the \"App\").\n\n"
        )

        // Section 1
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("1. Information We Collect\n\n")
        }

        append("We may collect the following types of information:\n\n")

        append("• Account Information: Email address, username, and authentication credentials used to create and manage your account.\n")
        append("• Profile Information: Optional information such as name, native language, learning preferences, and app settings.\n")
        append("• Learning Data: Progress in lessons, completed exercises, quiz results, streaks, achievements, and badges.\n")
        append("• Usage Data: Interaction with features, time spent on lessons, and app performance metrics.\n")
        append("• Device Information: Device model, operating system version, language settings, and anonymized identifiers used to improve compatibility and performance.\n\n")

        // Section 2
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("2. How We Use Your Information\n\n")
        }

        append("We use the collected information to:\n\n")

        append("• Provide, operate, and maintain the Firelingo App.\n")
        append("• Personalize learning content and improve the learning experience.\n")
        append("• Track progress and display statistics and achievements.\n")
        append("• Improve app functionality, performance, and stability.\n")
        append("• Communicate important updates, service-related messages, or support responses.\n")
        append("• Ensure security and prevent misuse of the App.\n\n")

        // Section 3
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("3. Data Storage and Security\n\n")
        }

        append(
            "We take reasonable technical and organizational measures to protect your information " +
                    "against unauthorized access, loss, or misuse. However, no method of transmission or storage " +
                    "is 100% secure.\n\n"
        )

        // Section 4
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("4. Data Sharing\n\n")
        }

        append("Firelingo does not sell your personal data. We may share limited data only when necessary:\n\n")
        append("• With service providers that help us operate the App (e.g., hosting or analytics), under strict confidentiality agreements.\n")
        append("• When required by law or legal process.\n\n")

        // Section 5
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("5. Your Rights\n\n")
        }

        append("Depending on your location, you may have the right to:\n\n")
        append("• Access, update, or delete your personal data.\n")
        append("• Request a copy of the data we store about you.\n")
        append("• Withdraw consent for optional data collection.\n\n")

        // Section 6
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("6. Children's Privacy\n\n")
        }

        append(
            "Firelingo is not intended for children under the age of 13 without parental consent. " +
                    "We do not knowingly collect personal data from children without authorization.\n\n"
        )

        // Section 7
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("7. Changes to This Policy\n\n")
        }

        append(
            "We may update this Privacy Policy from time to time. " +
                    "Changes will be effective once posted within the App.\n\n"
        )

        // Section 8
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("8. Contact Us\n\n")
        }

        append(
            "If you have questions about this Privacy Policy, please contact us through the support section of the Firelingo App."
        )
    }


    val TERMS_OF_SERVICE = buildAnnotatedString {

        withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
            append("Effective Date: December 20, 2024\n")
        }

        append("\n\n")

        append(
            "Welcome to Firelingo! These Terms of Service (\"Terms\") govern your access to and use of " +
                    "the Firelingo mobile application and related services (the \"App\").\n\n"
        )

        append(
            "By accessing or using Firelingo, you agree to be bound by these Terms. " +
                    "If you do not agree, you may not use the App.\n\n"
        )

        // Section 1
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("1. Use of the App\n\n")
        }

        append(
            "Firelingo is provided for personal, non-commercial educational use only. " +
                    "You agree to use the App in a lawful manner and not to misuse its features or content.\n\n"
        )

        // Section 2
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("2. User Accounts\n\n")
        }

        append("• You are responsible for maintaining the confidentiality of your account credentials.\n")
        append("• You are responsible for all activity that occurs under your account.\n")
        append("• You agree to provide accurate and up-to-date information.\n\n")

        // Section 3
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("3. Content and Intellectual Property\n\n")
        }

        append(
            "All content available in Firelingo, including text, audio, lessons, graphics, logos, " +
                    "and software, is the property of Firelingo or its licensors and is protected by copyright " +
                    "and intellectual property laws.\n\n"
        )

        append(
            "You may not copy, modify, distribute, or reverse-engineer any part of the App without " +
                    "prior written permission.\n\n"
        )

        // Section 4
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("4. User-Generated Data\n\n")
        }

        append(
            "Learning progress and user activity data remain associated with your account. " +
                    "Firelingo may use aggregated and anonymized data for analytics and improvement purposes.\n\n"
        )

        // Section 5
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("5. Availability and Changes\n\n")
        }

        append(
            "We reserve the right to modify, suspend, or discontinue any part of the App at any time " +
                    "without prior notice.\n\n"
        )

        // Section 6
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("6. Limitation of Liability\n\n")
        }

        append(
            "Firelingo is provided \"as is\" without warranties of any kind. " +
                    "We are not liable for any indirect, incidental, or consequential damages arising from the " +
                    "use of the App.\n\n"
        )

        // Section 7
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("7. Termination\n\n")
        }

        append(
            "We may suspend or terminate your account if you violate these Terms or misuse the App.\n\n"
        )

        // Section 8
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("8. Governing Law\n\n")
        }

        append(
            "These Terms shall be governed by and interpreted in accordance with applicable laws, " +
                    "without regard to conflict of law principles.\n\n"
        )

        // Section 9
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("9. Contact\n\n")
        }

        append(
            "For questions regarding these Terms, please contact us through the Firelingo App support channels."
        )
    }
}
