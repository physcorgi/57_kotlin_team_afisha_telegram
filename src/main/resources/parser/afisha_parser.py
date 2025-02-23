import json
import sys
import requests
from bs4 import BeautifulSoup
from datetime import datetime
import logging
import re

# Настраиваем логирование в stderr
logging.basicConfig(stream=sys.stderr, level=logging.INFO)
logger = logging.getLogger(__name__)

class AfishaParser:
    def __init__(self):
        self.base_url = "https://afisha.yandex.ru"
        self.headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36'
        }

    def parse_events(self):
        try:
            # Парсим разные категории для большего охвата
            categories = [
                "/moscow/concert",
                "/moscow/theatre",
                "/moscow/exhibition",
                "/moscow/cinema"
            ]
            
            events = []
            for category in categories:
                url = f"{self.base_url}{category}"
                logger.info(f"Parsing category: {url}")
                
                response = requests.get(url, headers=self.headers)
                response.raise_for_status()
                
                soup = BeautifulSoup(response.text, 'html.parser')
                logger.info(f"Page content length: {len(response.text)}")
                
                # Выводим часть HTML для анализа структуры
                logger.info(f"Sample HTML: {soup.prettify()[:500]}")
                
                # Пробуем разные селекторы
                event_cards = (
                    soup.find_all('div', class_=re.compile(r'event-card|EventCard')) or
                    soup.find_all('div', {'data-component': re.compile(r'Event|Card')}) or
                    soup.find_all('article') or
                    soup.find_all('div', class_=re.compile(r'Card|Item|Event', re.I))
                )
                
                logger.info(f"Found {len(event_cards)} events in category {category}")

                for card in event_cards[:3]:  # Берем по 3 мероприятия из каждой категории
                    try:
                        # Логируем структуру карточки для отладки
                        logger.info(f"Card HTML: {card.prettify()}")
                        
                        # Находим название (пробуем разные варианты)
                        title = ""
                        for title_selector in ['h1', 'h2', 'h3', 'h4', 
                                             {'class': re.compile(r'title|name', re.I)},
                                             {'data-component': re.compile(r'title|name', re.I)}]:
                            title_elem = card.find(title_selector)
                            if title_elem:
                                title = title_elem.text.strip()
                                break

                        # Находим описание
                        description = ""
                        desc_elem = card.find(class_=re.compile(r'description|desc|about', re.I))
                        if desc_elem:
                            description = desc_elem.text.strip()
                        
                        # Находим место проведения
                        location = "Москва"
                        loc_elem = card.find(class_=re.compile(r'location|address|venue', re.I))
                        if loc_elem:
                            location = loc_elem.text.strip()

                        # Находим цену
                        price = 0.0
                        price_elem = card.find(class_=re.compile(r'price|cost', re.I))
                        if price_elem:
                            price = self._extract_price(price_elem.text)

                        # Определяем категорию
                        category_name = category.split('/')[-1].capitalize()

                        if title:  # Добавляем только если есть название
                            event = {
                                'name': title,
                                'category': category_name,
                                'description': description or "Подробности на сайте мероприятия",
                                'location': location,
                                'price': price,
                                'dateTime': datetime.now().isoformat()
                            }
                            events.append(event)
                            logger.info(f"Added event: {title}")

                    except Exception as e:
                        logger.error(f"Error parsing event card: {e}")

            if not events:
                logger.warning("No events found, adding test data")
                events = self._get_test_events()

            logger.info(f"Successfully parsed {len(events)} events")
            print(json.dumps(events, ensure_ascii=False))
            return events

        except Exception as e:
            logger.error(f"Error during parsing: {e}")
            return self._get_test_events()

    def _extract_price(self, price_text):
        try:
            numbers = re.findall(r'\d+', price_text.replace(' ', ''))
            if not numbers:
                return 0.0
            numbers = [float(n) for n in numbers]
            return sum(numbers) / len(numbers)
        except:
            return 0.0

    def _get_test_events(self):
        return [
            {
                'name': 'Концерт классической музыки',
                'category': 'Concert',
                'description': 'Великолепный концерт классической музыки в исполнении симфонического оркестра',
                'location': 'Московская консерватория',
                'price': 1500.0,
                'dateTime': datetime.now().isoformat()
            },
            {
                'name': 'Выставка современного искусства',
                'category': 'Exhibition',
                'description': 'Уникальная выставка работ современных художников',
                'location': 'Третьяковская галерея',
                'price': 800.0,
                'dateTime': datetime.now().isoformat()
            },
            {
                'name': 'Стендап шоу',
                'category': 'Theatre',
                'description': 'Вечер юмора с известными комиками',
                'location': 'Клуб Stand Up',
                'price': 2000.0,
                'dateTime': datetime.now().isoformat()
            }
        ]

if __name__ == "__main__":
    parser = AfishaParser()
    parser.parse_events() 