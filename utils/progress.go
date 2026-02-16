package utils

import (
	"fmt"
	"time"

	"github.com/cheggaaa/pb/v3"
)

type Bar struct {
	pb *pb.ProgressBar
}

func NewBar(count int, MyStrStart, MyStrEnd string) *Bar {
	tmpl := fmt.Sprintf(`{{counters . }} {{ bar . "[" "-" (cycle . "↖" "↗" "↘" "↙" ) "_" "]"}} %s {{string . "MyStr" | green}} %s {{rtime . | blue}}`, MyStrStart, MyStrEnd)
	bar := pb.ProgressBarTemplate(tmpl).Start(count)
	return &Bar{pb: bar}
}

func (b *Bar) Grow(num int, MyStrVal string) {
	b.pb.Set("MyStr", MyStrVal).Add(num)
}

func (b *Bar) Done() {
	b.pb.Finish()
}

func main() {
	total := 100
	bar := NewBar(total, "Start", "End")

	for i := 0; i < total; i++ {
		bar.Grow(1, fmt.Sprintf("Progress %d/%d", i+1, total))
		time.Sleep(50 * time.Millisecond)
	}

	bar.Done()
}
