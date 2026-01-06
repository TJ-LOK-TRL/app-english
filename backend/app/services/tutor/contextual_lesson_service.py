from core.models.tutor import LessonResponseStruct
from core.interfaces.ichat_service import IChatService

class ContextualLessonService:
    def __init__(self, chat_service: IChatService) -> None:
        self.chat_service = chat_service
        self.system_message = (
            "You are an expert English teacher. Based on the user's provided context "
            "(GPS location, text description, or scenario), generate a practical English lesson. "
            "Identify 5 useful words or phrases related to that context. "
            "Return the original text in Portuguese and the translation in English."
        )

    def generate_lesson(self, context_data: str) -> LessonResponseStruct:
        prompt = f"Context for the lesson: {context_data}"
        
        response = self.chat_service.generate_parsed(
            prompt=prompt,
            response_schema=LessonResponseStruct,
            system_message=self.system_message
        )
        
        return response